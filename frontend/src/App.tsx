import React, { FormEvent, useEffect, useMemo, useState } from 'react';
import RefreshCcwIcon from 'lucide-react/dist/esm/icons/refresh-ccw.js';
import SaveIcon from 'lucide-react/dist/esm/icons/save.js';
import WrenchIcon from 'lucide-react/dist/esm/icons/wrench.js';

type Car = {
  id: number;
  licensePlate: string;
  brand: string;
  model: string;
  owner: { fullName: string; phoneNumber?: string; email?: string };
  technicalProfile?: TechnicalProfile | null;
};

type TechnicalProfile = {
  engineOilType?: string;
  tireBrand?: string;
  tireSize?: string;
  batteryType?: string;
  brakeFluidType?: string;
  transmissionOilType?: string;
};

type CatalogService = { id: number; title: string; description: string };
type ServiceStatus = 'PENDING' | 'IN_PROGRESS' | 'DONE';
type ServiceAction = {
  id: number;
  car: { id: number; licensePlate?: string };
  service: CatalogService;
  status: ServiceStatus;
  technicianReport?: string;
  createdAt: string;
  finishedAt?: string | null;
  version: number;
};

const nextStatuses: Record<ServiceStatus, ServiceStatus[]> = {
  PENDING: ['IN_PROGRESS'],
  IN_PROGRESS: ['DONE'],
  DONE: []
};

type IconComponent = React.ComponentType<{ size?: number }>;

function normalizeIcon(icon: IconComponent | { default: IconComponent }): IconComponent {
  return 'default' in icon ? icon.default : icon;
}

const RefreshCcw = normalizeIcon(RefreshCcwIcon);
const Save = normalizeIcon(SaveIcon);
const Wrench = normalizeIcon(WrenchIcon);

async function api<T>(url: string, init?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    ...init,
    headers: { 'Content-Type': 'application/json', ...(init?.headers ?? {}) }
  });
  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: response.statusText }));
    throw new Error(error.message ?? response.statusText);
  }
  return response.json();
}

export function App() {
  const [cars, setCars] = useState<Car[]>([]);
  const [catalog, setCatalog] = useState<CatalogService[]>([]);
  const [actions, setActions] = useState<ServiceAction[]>([]);
  const [selectedCarId, setSelectedCarId] = useState<number | ''>('');
  const [statusFilter, setStatusFilter] = useState<ServiceStatus | ''>('');
  const [message, setMessage] = useState('');
  const [carForm, setCarForm] = useState({
    licensePlate: '',
    brand: '',
    model: '',
    ownerName: '',
    phoneNumber: '',
    email: '',
    engineOilType: '',
    tireBrand: '',
    tireSize: '',
    brakeFluidType: ''
  });
  const [serviceForm, setServiceForm] = useState({ carId: '', serviceId: '' });
  const [editing, setEditing] = useState<Record<number, { status: ServiceStatus | ''; report: string }>>({});

  const selectedCar = useMemo(
    () => cars.find((car) => car.id === selectedCarId),
    [cars, selectedCarId]
  );

  async function refresh() {
    const query = new URLSearchParams();
    if (selectedCarId) query.set('carId', String(selectedCarId));
    if (statusFilter) query.set('status', statusFilter);
    const [carData, catalogData, actionData] = await Promise.all([
      api<Car[]>('/api/cars'),
      api<CatalogService[]>('/api/services/catalog'),
      api<ServiceAction[]>(`/api/services${query.toString() ? `?${query}` : ''}`)
    ]);
    setCars(carData);
    setCatalog(catalogData);
    setActions(actionData);
  }

  useEffect(() => {
    refresh().catch((error) => setMessage(error.message));
  }, [selectedCarId, statusFilter]);

  async function createCar(event: FormEvent) {
    event.preventDefault();
    setMessage('');
    const technicalProfile = carForm.engineOilType || carForm.tireBrand || carForm.tireSize || carForm.brakeFluidType
      ? {
          engineOilType: carForm.engineOilType,
          tireBrand: carForm.tireBrand,
          tireSize: carForm.tireSize,
          brakeFluidType: carForm.brakeFluidType
        }
      : null;
    try {
      await api<Car>('/api/cars', {
        method: 'POST',
        body: JSON.stringify({
          licensePlate: carForm.licensePlate,
          brand: carForm.brand,
          model: carForm.model,
          owner: { fullName: carForm.ownerName, phoneNumber: carForm.phoneNumber, email: carForm.email },
          technicalProfile
        })
      });
      setCarForm({ licensePlate: '', brand: '', model: '', ownerName: '', phoneNumber: '', email: '', engineOilType: '', tireBrand: '', tireSize: '', brakeFluidType: '' });
      await refresh();
    } catch (error) {
      setMessage((error as Error).message);
    }
  }

  async function createServiceAction(event: FormEvent) {
    event.preventDefault();
    const car = cars.find((item) => item.id === Number(serviceForm.carId));
    try {
      await api<ServiceAction>('/api/services', {
        method: 'POST',
        body: JSON.stringify({
          carId: Number(serviceForm.carId),
          carLicensePlate: car?.licensePlate,
          serviceId: Number(serviceForm.serviceId)
        })
      });
      setServiceForm({ carId: '', serviceId: '' });
      await refresh();
    } catch (error) {
      setMessage((error as Error).message);
    }
  }

  async function updateAction(action: ServiceAction) {
    const draft = editing[action.id];
    if (!draft) return;
    try {
      await api<ServiceAction>(`/api/services/${action.id}`, {
        method: 'PUT',
        body: JSON.stringify({
          status: draft.status || action.status,
          technicianReport: draft.report,
          version: action.version
        })
      });
      await refresh();
    } catch (error) {
      setMessage((error as Error).message);
      await refresh();
    }
  }

  return (
    <main className="min-h-screen">
      <header className="border-b border-line bg-white">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
          <div>
            <h1 className="text-2xl font-semibold tracking-normal">Car Service Manager</h1>
            <p className="text-sm text-steel">Service operations, technician context, and audit-ready workflows.</p>
          </div>
          <button className="inline-flex items-center gap-2 rounded-md border border-line px-3 py-2 text-sm" onClick={() => refresh()}>
            <RefreshCcw size={16} /> Refresh
          </button>
        </div>
      </header>

      <div className="mx-auto grid max-w-7xl grid-cols-1 gap-5 px-6 py-6 lg:grid-cols-[380px_1fr]">
        <aside className="space-y-5">
          <section className="rounded-md border border-line bg-white p-4">
            <h2 className="mb-3 text-base font-semibold">Create car</h2>
            <form className="grid gap-3" onSubmit={createCar}>
              <input className="rounded-md border border-line px-3 py-2" placeholder="License plate" value={carForm.licensePlate} onChange={(e) => setCarForm({ ...carForm, licensePlate: e.target.value })} />
              <div className="grid grid-cols-2 gap-3">
                <input className="rounded-md border border-line px-3 py-2" placeholder="Brand" value={carForm.brand} onChange={(e) => setCarForm({ ...carForm, brand: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="Model" value={carForm.model} onChange={(e) => setCarForm({ ...carForm, model: e.target.value })} />
              </div>
              <input className="rounded-md border border-line px-3 py-2" placeholder="Owner full name" value={carForm.ownerName} onChange={(e) => setCarForm({ ...carForm, ownerName: e.target.value })} />
              <div className="grid grid-cols-2 gap-3">
                <input className="rounded-md border border-line px-3 py-2" placeholder="Phone" value={carForm.phoneNumber} onChange={(e) => setCarForm({ ...carForm, phoneNumber: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="Email" value={carForm.email} onChange={(e) => setCarForm({ ...carForm, email: e.target.value })} />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <input className="rounded-md border border-line px-3 py-2" placeholder="Engine oil" value={carForm.engineOilType} onChange={(e) => setCarForm({ ...carForm, engineOilType: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="Tire brand" value={carForm.tireBrand} onChange={(e) => setCarForm({ ...carForm, tireBrand: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="Tire size" value={carForm.tireSize} onChange={(e) => setCarForm({ ...carForm, tireSize: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="Brake fluid" value={carForm.brakeFluidType} onChange={(e) => setCarForm({ ...carForm, brakeFluidType: e.target.value })} />
              </div>
              <button className="inline-flex items-center justify-center gap-2 rounded-md bg-accent px-3 py-2 text-sm font-medium text-white">
                <Save size={16} /> Save car
              </button>
            </form>
          </section>

          <section className="rounded-md border border-line bg-white p-4">
            <h2 className="mb-3 text-base font-semibold">Create service action</h2>
            <form className="grid gap-3" onSubmit={createServiceAction}>
              <select className="rounded-md border border-line px-3 py-2" value={serviceForm.carId} onChange={(e) => setServiceForm({ ...serviceForm, carId: e.target.value })}>
                <option value="">Select car</option>
                {cars.map((car) => <option key={car.id} value={car.id}>{car.licensePlate} - {car.brand} {car.model}</option>)}
              </select>
              <select className="rounded-md border border-line px-3 py-2" value={serviceForm.serviceId} onChange={(e) => setServiceForm({ ...serviceForm, serviceId: e.target.value })}>
                <option value="">Select service</option>
                {catalog.map((service) => <option key={service.id} value={service.id}>{service.title}</option>)}
              </select>
              <button className="inline-flex items-center justify-center gap-2 rounded-md bg-ink px-3 py-2 text-sm font-medium text-white">
                <Wrench size={16} /> Create action
              </button>
            </form>
          </section>
        </aside>

        <section className="space-y-5">
          {message && <div role="alert" className="rounded-md border border-warn bg-white px-4 py-3 text-sm text-warn">{message}</div>}

          <div className="rounded-md border border-line bg-white">
            <div className="flex flex-wrap items-center justify-between gap-3 border-b border-line p-4">
              <h2 className="text-base font-semibold">Cars</h2>
              <select aria-label="Car filter" className="rounded-md border border-line px-3 py-2 text-sm" value={selectedCarId} onChange={(e) => setSelectedCarId(e.target.value ? Number(e.target.value) : '')}>
                <option value="">All cars</option>
                {cars.map((car) => <option key={car.id} value={car.id}>{car.licensePlate}</option>)}
              </select>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full min-w-[720px] text-left text-sm">
                <thead className="bg-panel text-steel">
                  <tr><th className="p-3">License Plate</th><th>Brand</th><th>Model</th><th>Owner</th></tr>
                </thead>
                <tbody>
                  {cars.map((car) => (
                    <tr key={car.id} className="border-t border-line">
                      <td className="p-3 font-medium">{car.licensePlate}</td><td>{car.brand}</td><td>{car.model}</td><td>{car.owner.fullName}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div className="rounded-md border border-line bg-white">
            <div className="flex flex-wrap items-center justify-between gap-3 border-b border-line p-4">
              <h2 className="text-base font-semibold">Services</h2>
              <select aria-label="Status filter" className="rounded-md border border-line px-3 py-2 text-sm" value={statusFilter} onChange={(e) => setStatusFilter(e.target.value as ServiceStatus | '')}>
                <option value="">All statuses</option>
                <option value="PENDING">Pending</option>
                <option value="IN_PROGRESS">In progress</option>
                <option value="DONE">Done</option>
              </select>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full min-w-[900px] text-left text-sm">
                <thead className="bg-panel text-steel">
                  <tr><th className="p-3">Title</th><th>Car</th><th>Status</th><th>Report</th><th>Actions</th></tr>
                </thead>
                <tbody>
                  {actions.map((action) => {
                    const draft = editing[action.id] ?? { status: '', report: action.technicianReport ?? '' };
                    return (
                      <tr key={action.id} className="border-t border-line align-top">
                        <td className="p-3 font-medium">{action.service.title}</td>
                        <td>{action.car.licensePlate ?? action.car.id}</td>
                        <td>
                          <select aria-label={`Next status for ${action.service.title}`} className="rounded-md border border-line px-2 py-1" value={draft.status} disabled={nextStatuses[action.status].length === 0} onChange={(e) => setEditing({ ...editing, [action.id]: { ...draft, status: e.target.value as ServiceStatus } })}>
                            <option value="">{action.status}</option>
                            {nextStatuses[action.status].map((status) => <option key={status} value={status}>{status}</option>)}
                          </select>
                        </td>
                        <td>
                          <textarea className="min-h-20 w-full rounded-md border border-line px-2 py-1" value={draft.report} onChange={(e) => setEditing({ ...editing, [action.id]: { ...draft, report: e.target.value } })} />
                        </td>
                        <td>
                          <button aria-label={`Update ${action.service.title}`} className="rounded-md border border-line px-3 py-2 text-sm" onClick={() => updateAction(action)}>Update</button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>

          <div className="rounded-md border border-line bg-white p-4">
            <h2 className="mb-3 text-base font-semibold">Technician context</h2>
            {selectedCar ? (
              <div className="grid gap-3 md:grid-cols-2">
                {[
                  ['Engine Oil Type', selectedCar.technicalProfile?.engineOilType],
                  ['Tire Brand', selectedCar.technicalProfile?.tireBrand],
                  ['Tire Size', selectedCar.technicalProfile?.tireSize],
                  ['Brake Fluid', selectedCar.technicalProfile?.brakeFluidType]
                ].map(([label, value]) => (
                  <div key={label} className="rounded-md border border-line bg-panel p-3">
                    <div className="text-xs uppercase text-steel">{label}</div>
                    <div className="font-medium">{value || 'Not recorded'}</div>
                  </div>
                ))}
                <div className="md:col-span-2">
                  <div className="text-xs uppercase text-steel">Recent technician notes</div>
                  <p className="mt-1 text-sm">{actions.find((item) => item.status === 'DONE' && item.technicianReport)?.technicianReport ?? 'No completed report for the selected filters.'}</p>
                </div>
              </div>
            ) : (
              <p className="text-sm text-steel">Select a car to inspect technical profile and recent notes.</p>
            )}
          </div>
        </section>
      </div>
    </main>
  );
}
