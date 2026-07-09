#!/usr/bin/env bash
set -euo pipefail

CAR_SERVICE_URL="${CAR_SERVICE_URL:-http://localhost:8081}"
SERVICE_SERVICE_URL="${SERVICE_SERVICE_URL:-http://localhost:8082}"
MYSQL_USER="${MYSQL_USER:-car_user}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-car_pass}"
AUDIT_SERVICE_DB="${AUDIT_SERVICE_DB:-audit_service}"

require_command() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Missing required command: $1" >&2
    exit 1
  fi
}

json_field() {
  local expression="$1"
  node -e "const input = JSON.parse(require('fs').readFileSync(0, 'utf8')); const value = ${expression}; if (value === undefined || value === null || value === '') process.exit(1); console.log(value);"
}

wait_for_http() {
  local url="$1"
  local label="$2"

  for _ in $(seq 1 30); do
    if curl -fsS "$url" >/dev/null; then
      return 0
    fi
    sleep 2
  done

  echo "Timed out waiting for ${label}: ${url}" >&2
  exit 1
}

require_command curl
require_command docker
require_command node

wait_for_http "${CAR_SERVICE_URL}/actuator/health" "car-service"
wait_for_http "${SERVICE_SERVICE_URL}/actuator/health" "service-service"

run_id="$(date +%s)"
plate_suffix="${run_id: -4}"
license_plate="34 MVP ${plate_suffix}"

car_response="$(
  curl -fsS \
    -H "Content-Type: application/json" \
    -d "{
      \"licensePlate\":\"${license_plate}\",
      \"model\":\"Corolla\",
      \"brand\":\"Toyota\",
      \"owner\":{\"fullName\":\"MVP Smoke Owner\",\"phoneNumber\":\"+905550000000\",\"email\":\"smoke@example.com\"},
      \"technicalProfile\":{\"engineOilType\":\"5W-30\",\"tireBrand\":\"Michelin\",\"tireSize\":\"205/55 R16\"}
    }" \
    "${CAR_SERVICE_URL}/api/cars"
)"
car_id="$(printf '%s' "$car_response" | json_field "input.id")"
created_plate="$(printf '%s' "$car_response" | json_field "input.licensePlate")"

if [ "$created_plate" != "$license_plate" ]; then
  echo "Created car plate mismatch: expected ${license_plate}, got ${created_plate}" >&2
  exit 1
fi

catalog_response="$(curl -fsS "${SERVICE_SERVICE_URL}/api/services/catalog")"
service_id="$(printf '%s' "$catalog_response" | json_field "input[0]?.id")"

action_response="$(
  curl -fsS \
    -H "Content-Type: application/json" \
    -d "{\"carId\":${car_id},\"carLicensePlate\":\"${license_plate}\",\"serviceId\":${service_id}}" \
    "${SERVICE_SERVICE_URL}/api/services"
)"
action_id="$(printf '%s' "$action_response" | json_field "input.id")"
action_version="$(printf '%s' "$action_response" | json_field "input.version")"
action_status="$(printf '%s' "$action_response" | json_field "input.status")"

if [ "$action_status" != "PENDING" ]; then
  echo "Created service action status mismatch: expected PENDING, got ${action_status}" >&2
  exit 1
fi

updated_action_response="$(
  curl -fsS \
    -X PUT \
    -H "Content-Type: application/json" \
    -d "{\"status\":\"IN_PROGRESS\",\"technicianReport\":\"Smoke test started\",\"version\":${action_version}}" \
    "${SERVICE_SERVICE_URL}/api/services/${action_id}"
)"
updated_action_status="$(printf '%s' "$updated_action_response" | json_field "input.status")"

if [ "$updated_action_status" != "IN_PROGRESS" ]; then
  echo "Updated service action status mismatch: expected IN_PROGRESS, got ${updated_action_status}" >&2
  exit 1
fi

filtered_response="$(curl -fsS "${SERVICE_SERVICE_URL}/api/services?carId=${car_id}&status=IN_PROGRESS")"
filtered_count="$(printf '%s' "$filtered_response" | json_field "input.filter(action => action.id === ${action_id}).length")"

if [ "$filtered_count" != "1" ]; then
  echo "Updated service action was not returned by carId/status filter" >&2
  exit 1
fi

for _ in $(seq 1 20); do
  audit_count="$(
    docker compose exec -T mysql \
      mysql -u"${MYSQL_USER}" -p"${MYSQL_PASSWORD}" --batch --skip-column-names "${AUDIT_SERVICE_DB}" \
      -e "SELECT COUNT(*) FROM audit_log WHERE (event_type = 'CAR_CREATED' AND entity_id = ${car_id}) OR (event_type IN ('SERVICE_ACTION_CREATED', 'SERVICE_ACTION_STATUS_CHANGED') AND entity_id = ${action_id});"
  )"

  if [ "${audit_count}" -ge 3 ]; then
    echo "Smoke test passed: car=${car_id}, serviceAction=${action_id}, auditEvents=${audit_count}"
    exit 0
  fi

  sleep 2
done

echo "Timed out waiting for audit events for car=${car_id}, serviceAction=${action_id}" >&2
exit 1
