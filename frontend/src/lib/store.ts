import { writable, type Writable } from 'svelte/store';

import { browser } from '$app/environment';
import type { UrlModel } from '../routes/Models';

let tmpJWT: string = '';
let tmpClip: string = '';

if (browser) {
	tmpJWT = localStorage.getItem('ZIPITJWT') ?? '';
	tmpClip = localStorage.getItem('ZIPITCLIP') ?? 'true';
}

export const JWT = writable(tmpJWT);
export const isLoggedIn = writable(false);
export const MONITOR_CLIPBOARD = writable(tmpClip === 'true');

// Defining variables for global popup
export const showPopUp = writable(false);
export const heading = writable('Popup');
export const content = writable('Popup Content');
export const okFunction = writable(() => {});
export const okText = writable('Okay');

export const listOfUrls: Writable<UrlModel[]> = writable([]);

MONITOR_CLIPBOARD.subscribe((value) => {
	if (browser) localStorage.setItem('ZIPITCLIP', value ? 'true' : 'false');
});

JWT.subscribe((value) => {
	if (browser) localStorage.setItem('ZIPITJWT', value);
});
