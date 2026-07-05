import React from 'react';
import assert from 'node:assert/strict';
import { afterEach, beforeEach, describe, it } from 'node:test';
import { cleanup, render, screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { App } from './App';

const cars = [
  {
    id: 1,
    licensePlate: '34 ABC 123',
    brand: 'Toyota',
    model: 'Corolla',
    owner: { fullName: 'Mehmet Altindal' },
    technicalProfile: { engineOilType: '5W-30', tireBrand: 'Michelin' }
  },
  {
    id: 2,
    licensePlate: '06 XYZ 789',
    brand: 'Ford',
    model: 'Focus',
    owner: { fullName: 'Ayse Yilmaz' },
    technicalProfile: null
  }
];

const catalog = [
  { id: 1, title: 'Oil Change', description: 'Engine oil and filter' },
  { id: 2, title: 'Brake Check', description: 'Brake system inspection' }
];

const actions = [
  {
    id: 10,
    car: { id: 1, licensePlate: '34 ABC 123' },
    service: catalog[0],
    status: 'PENDING',
    technicianReport: '',
    createdAt: '2026-07-05T10:00:00Z',
    finishedAt: null,
    version: 1
  },
  {
    id: 11,
    car: { id: 2, licensePlate: '06 XYZ 789' },
    service: catalog[1],
    status: 'DONE',
    technicianReport: 'Pads checked',
    createdAt: '2026-07-05T11:00:00Z',
    finishedAt: '2026-07-05T12:00:00Z',
    version: 2
  }
] as const;

type FetchCall = [RequestInfo | URL, RequestInit | undefined];

function jsonResponse(body: unknown, status = 200) {
  return Promise.resolve(
    new Response(JSON.stringify(body), {
      status,
      headers: { 'Content-Type': 'application/json' }
    })
  );
}

function installFetchMock() {
  const calls: FetchCall[] = [];
  const fetchMock = (input: RequestInfo | URL, init?: RequestInit) => {
    calls.push([input, init]);
    const url = String(input);
    if (init?.method === 'POST' && url === '/api/cars') {
      return jsonResponse({ message: 'Plaka formati gecersiz' }, 400);
    }
    if (init?.method === 'PUT' && url === '/api/services/10') {
      return jsonResponse({ message: 'Kayit guncel degil, yeniden deneyin' }, 409);
    }
    if (url === '/api/cars') return jsonResponse(cars);
    if (url === '/api/services/catalog') return jsonResponse(catalog);
    if (url.startsWith('/api/services')) return jsonResponse(actions);
    return jsonResponse({ message: 'Unexpected request' }, 500);
  };

  globalThis.fetch = fetchMock as typeof fetch;
  return { calls };
}

describe('App', () => {
  beforeEach(() => {
    document.body.innerHTML = '';
    installFetchMock();
  });

  afterEach(() => {
    cleanup();
  });

  it('renders backend validation errors from car creation', async () => {
    const user = userEvent.setup({ document });
    render(<App />);

    await screen.findAllByText('34 ABC 123');
    await user.type(screen.getByPlaceholderText('License plate'), 'BAD!');
    await user.click(screen.getByRole('button', { name: /save car/i }));

    assert.match((await screen.findByRole('alert')).textContent ?? '', /Plaka formati gecersiz/);
  });

  it('shows conflict message and refreshes after stale service action update', async () => {
    const user = userEvent.setup({ document });
    const fetchMock = installFetchMock();
    render(<App />);

    await screen.findAllByText('Oil Change');
    await user.selectOptions(screen.getByLabelText('Next status for Oil Change'), 'IN_PROGRESS');
    await user.click(screen.getByRole('button', { name: 'Update Oil Change' }));

    assert.match((await screen.findByRole('alert')).textContent ?? '', /Kayit guncel degil/);
    await waitFor(() => {
      assert.ok(fetchMock.calls.filter(([url]) => String(url).startsWith('/api/services')).length > 2);
    });
  });

  it('shows only valid next statuses for each service action', async () => {
    render(<App />);

    const pendingStatus = await screen.findByLabelText('Next status for Oil Change');
    assert.ok(within(pendingStatus).getByRole('option', { name: 'PENDING' }));
    assert.ok(within(pendingStatus).getByRole('option', { name: 'IN_PROGRESS' }));
    assert.equal(within(pendingStatus).queryByRole('option', { name: 'DONE' }), null);

    const doneStatus = screen.getByLabelText('Next status for Brake Check') as HTMLSelectElement;
    assert.equal(doneStatus.disabled, true);
  });

  it('requests service actions with car and status filters', async () => {
    const user = userEvent.setup({ document });
    const fetchMock = installFetchMock();
    render(<App />);

    await screen.findAllByText('Oil Change');
    await user.selectOptions(screen.getByLabelText('Car filter'), '1');
    await user.selectOptions(screen.getByLabelText('Status filter'), 'PENDING');

    await waitFor(() => {
      assert.ok(fetchMock.calls.some(([url]) => String(url) === '/api/services?carId=1&status=PENDING'));
    });
  });
});
