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

const statusLabels: Record<ServiceStatus, string> = {
  PENDING: 'Beklemede',
  IN_PROGRESS: 'Devam ediyor',
  DONE: 'Tamamlandı'
};

const serviceTitleLabels: Record<string, string> = {
  'Oil Change': 'Yağ değişimi',
  Inspection: 'Genel kontrol',
  'Tire Change': 'Lastik değişimi',
  'Brake Check': 'Fren kontrolü'
};

const errorMessageLabels: Record<string, string> = {
  'License plate already exists': 'Bu plaka zaten kayıtlı',
  'Invalid license plate': 'Plaka formatı geçersiz',
  'Service action was updated by another user. Refresh the row and try again.':
    'Servis işlemi başka bir kullanıcı tarafından güncellendi. Satırı yenileyip tekrar deneyin.',
  'A car can have at most 2 IN_PROGRESS service actions.':
    'Bir araçta aynı anda en fazla 2 servis işlemi devam ediyor olabilir.',
  'Service action not found': 'Servis işlemi bulunamadı',
  'Service catalog entry not found': 'Servis katalog kaydı bulunamadı',
  'Car not found': 'Araç bulunamadı',
  'Bad Request': 'Geçersiz istek',
  Unauthorized: 'Yetkisiz işlem',
  Forbidden: 'Erişim reddedildi',
  'Not Found': 'Kayıt bulunamadı',
  Conflict: 'Çakışma oluştu',
  'Internal Server Error': 'Sunucu hatası'
};

type IconComponent = React.ComponentType<{ size?: number }>;

function normalizeIcon(icon: IconComponent | { default: IconComponent }): IconComponent {
  return 'default' in icon ? icon.default : icon;
}

const RefreshCcw = normalizeIcon(RefreshCcwIcon);
const Save = normalizeIcon(SaveIcon);
const Wrench = normalizeIcon(WrenchIcon);

function formatStatus(status: ServiceStatus) {
  return statusLabels[status];
}

function formatServiceTitle(title: string) {
  return serviceTitleLabels[title] ?? title;
}

function formatErrorMessage(message: string) {
  const exact = errorMessageLabels[message];
  if (exact) return exact;

  const partialMatch = Object.entries(errorMessageLabels).find(([source]) => message.startsWith(source));
  if (partialMatch) {
    return message.replace(partialMatch[0], partialMatch[1]);
  }

  return message;
}

async function api<T>(url: string, init?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    ...init,
    headers: { 'Content-Type': 'application/json', ...(init?.headers ?? {}) }
  });
  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: response.statusText }));
    throw new Error(formatErrorMessage(error.message ?? response.statusText));
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
            <h1 className="text-2xl font-semibold tracking-normal">Araç Servis Yönetimi</h1>
            <p className="text-sm text-steel">Servis operasyonları, teknisyen bağlamı ve audit uyumlu iş akışları.</p>
          </div>
          <button className="inline-flex items-center gap-2 rounded-md border border-line px-3 py-2 text-sm" onClick={() => refresh()}>
            <RefreshCcw size={16} /> Yenile
          </button>
        </div>
      </header>

      <div className="mx-auto grid max-w-7xl grid-cols-1 gap-5 px-6 py-6 lg:grid-cols-[380px_1fr]">
        <aside className="space-y-5">
          <section className="rounded-md border border-line bg-white p-4">
            <h2 className="mb-3 text-base font-semibold">Araç oluştur</h2>
            <form className="grid gap-3" onSubmit={createCar}>
              <input className="rounded-md border border-line px-3 py-2" placeholder="Plaka" value={carForm.licensePlate} onChange={(e) => setCarForm({ ...carForm, licensePlate: e.target.value })} />
              <div className="grid grid-cols-2 gap-3">
                <input className="rounded-md border border-line px-3 py-2" placeholder="Marka" value={carForm.brand} onChange={(e) => setCarForm({ ...carForm, brand: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="Model" value={carForm.model} onChange={(e) => setCarForm({ ...carForm, model: e.target.value })} />
              </div>
              <input className="rounded-md border border-line px-3 py-2" placeholder="Araç sahibi adı soyadı" value={carForm.ownerName} onChange={(e) => setCarForm({ ...carForm, ownerName: e.target.value })} />
              <div className="grid grid-cols-2 gap-3">
                <input className="rounded-md border border-line px-3 py-2" placeholder="Telefon" value={carForm.phoneNumber} onChange={(e) => setCarForm({ ...carForm, phoneNumber: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="E-posta" value={carForm.email} onChange={(e) => setCarForm({ ...carForm, email: e.target.value })} />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <input className="rounded-md border border-line px-3 py-2" placeholder="Motor yağı" value={carForm.engineOilType} onChange={(e) => setCarForm({ ...carForm, engineOilType: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="Lastik markası" value={carForm.tireBrand} onChange={(e) => setCarForm({ ...carForm, tireBrand: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="Lastik ölçüsü" value={carForm.tireSize} onChange={(e) => setCarForm({ ...carForm, tireSize: e.target.value })} />
                <input className="rounded-md border border-line px-3 py-2" placeholder="Fren hidroliği" value={carForm.brakeFluidType} onChange={(e) => setCarForm({ ...carForm, brakeFluidType: e.target.value })} />
              </div>
              <button className="inline-flex items-center justify-center gap-2 rounded-md bg-accent px-3 py-2 text-sm font-medium text-white">
                <Save size={16} /> Aracı kaydet
              </button>
            </form>
          </section>

          <section className="rounded-md border border-line bg-white p-4">
            <h2 className="mb-3 text-base font-semibold">Servis işlemi oluştur</h2>
            <form className="grid gap-3" onSubmit={createServiceAction}>
              <select className="rounded-md border border-line px-3 py-2" value={serviceForm.carId} onChange={(e) => setServiceForm({ ...serviceForm, carId: e.target.value })}>
                <option value="">Araç seç</option>
                {cars.map((car) => <option key={car.id} value={car.id}>{car.licensePlate} - {car.brand} {car.model}</option>)}
              </select>
              <select className="rounded-md border border-line px-3 py-2" value={serviceForm.serviceId} onChange={(e) => setServiceForm({ ...serviceForm, serviceId: e.target.value })}>
                <option value="">Servis seç</option>
                {catalog.map((service) => <option key={service.id} value={service.id}>{formatServiceTitle(service.title)}</option>)}
              </select>
              <button className="inline-flex items-center justify-center gap-2 rounded-md bg-ink px-3 py-2 text-sm font-medium text-white">
                <Wrench size={16} /> İşlem oluştur
              </button>
            </form>
          </section>
        </aside>

        <section className="space-y-5">
          {message && <div role="alert" className="rounded-md border border-warn bg-white px-4 py-3 text-sm text-warn">{message}</div>}

          <div className="rounded-md border border-line bg-white">
            <div className="flex flex-wrap items-center justify-between gap-3 border-b border-line p-4">
              <h2 className="text-base font-semibold">Araçlar</h2>
              <select aria-label="Araç filtresi" className="rounded-md border border-line px-3 py-2 text-sm" value={selectedCarId} onChange={(e) => setSelectedCarId(e.target.value ? Number(e.target.value) : '')}>
                <option value="">Tüm araçlar</option>
                {cars.map((car) => <option key={car.id} value={car.id}>{car.licensePlate}</option>)}
              </select>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full min-w-[720px] text-left text-sm">
                <thead className="bg-panel text-steel">
                  <tr><th className="p-3">Plaka</th><th>Marka</th><th>Model</th><th>Sahip</th></tr>
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
              <h2 className="text-base font-semibold">Servis işlemleri</h2>
              <select aria-label="Durum filtresi" className="rounded-md border border-line px-3 py-2 text-sm" value={statusFilter} onChange={(e) => setStatusFilter(e.target.value as ServiceStatus | '')}>
                <option value="">Tüm durumlar</option>
                <option value="PENDING">{formatStatus('PENDING')}</option>
                <option value="IN_PROGRESS">{formatStatus('IN_PROGRESS')}</option>
                <option value="DONE">{formatStatus('DONE')}</option>
              </select>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full min-w-[900px] text-left text-sm">
                <thead className="bg-panel text-steel">
                  <tr><th className="p-3">Servis</th><th>Araç</th><th>Durum</th><th>Rapor</th><th>İşlemler</th></tr>
                </thead>
                <tbody>
                  {actions.map((action) => {
                    const draft = editing[action.id] ?? { status: '', report: action.technicianReport ?? '' };
                    return (
                      <tr key={action.id} className="border-t border-line align-top">
                        <td className="p-3 font-medium">{formatServiceTitle(action.service.title)}</td>
                        <td>{action.car.licensePlate ?? action.car.id}</td>
                        <td>
                          <select aria-label={`${formatServiceTitle(action.service.title)} için sonraki durum`} className="rounded-md border border-line px-2 py-1" value={draft.status} disabled={nextStatuses[action.status].length === 0} onChange={(e) => setEditing({ ...editing, [action.id]: { ...draft, status: e.target.value as ServiceStatus } })}>
                            <option value="">{formatStatus(action.status)}</option>
                            {nextStatuses[action.status].map((status) => <option key={status} value={status}>{formatStatus(status)}</option>)}
                          </select>
                        </td>
                        <td>
                          <textarea className="min-h-20 w-full rounded-md border border-line px-2 py-1" value={draft.report} onChange={(e) => setEditing({ ...editing, [action.id]: { ...draft, report: e.target.value } })} />
                        </td>
                        <td>
                          <button aria-label={`${formatServiceTitle(action.service.title)} güncelle`} className="rounded-md border border-line px-3 py-2 text-sm" onClick={() => updateAction(action)}>Güncelle</button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>

          <div className="rounded-md border border-line bg-white p-4">
            <h2 className="mb-3 text-base font-semibold">Teknisyen bağlamı</h2>
            {selectedCar ? (
              <div className="grid gap-3 md:grid-cols-2">
                {[
                  ['Motor yağı tipi', selectedCar.technicalProfile?.engineOilType],
                  ['Lastik markası', selectedCar.technicalProfile?.tireBrand],
                  ['Lastik ölçüsü', selectedCar.technicalProfile?.tireSize],
                  ['Fren hidroliği', selectedCar.technicalProfile?.brakeFluidType]
                ].map(([label, value]) => (
                  <div key={label} className="rounded-md border border-line bg-panel p-3">
                    <div className="text-xs uppercase text-steel">{label}</div>
                    <div className="font-medium">{value || 'Kayıt yok'}</div>
                  </div>
                ))}
                <div className="md:col-span-2">
                  <div className="text-xs uppercase text-steel">Son teknisyen notları</div>
                  <p className="mt-1 text-sm">{actions.find((item) => item.status === 'DONE' && item.technicianReport)?.technicianReport ?? 'Seçili filtreler için tamamlanmış rapor yok.'}</p>
                </div>
              </div>
            ) : (
              <p className="text-sm text-steel">Teknik profili ve son notları incelemek için bir araç seçin.</p>
            )}
          </div>
        </section>
      </div>
    </main>
  );
}
