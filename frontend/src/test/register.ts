import { JSDOM } from 'jsdom';

const dom = new JSDOM('<!doctype html><html><body></body></html>', {
  url: 'http://localhost'
});

Object.defineProperties(globalThis, {
  window: { value: dom.window },
  document: { value: dom.window.document },
  navigator: { value: dom.window.navigator },
  HTMLElement: { value: dom.window.HTMLElement },
  HTMLSelectElement: { value: dom.window.HTMLSelectElement },
  HTMLTextAreaElement: { value: dom.window.HTMLTextAreaElement },
  Event: { value: dom.window.Event },
  MouseEvent: { value: dom.window.MouseEvent }
});
