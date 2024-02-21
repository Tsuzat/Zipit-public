<script lang="ts">
	import zipitLogo from '$lib/images/icons/static/zipit_logo.svg';
	import { UrlModel } from './Models';
	import { formatDateTime, trimLongString } from './Utilities';

	import { PUBLIC_URL_PREFIX, PUBLIC_API_URL_DELETE } from '$env/static/public';
	import { content, heading, okFunction, JWT, showPopUp, listOfUrls, okText } from '$lib/store';
	import QRCode from 'qrcode';

	export let url: UrlModel;

	let shortURL = PUBLIC_URL_PREFIX + url.shortUrl;

	/**
	 * Invokes the deletion process of URL from backend
	 * but on customisation, can behave differently
	 */
	export let onDeleteFunction: Function = () => {
		heading.set('Are you sure?');
		content.set('Deleted URL can not be recovered. Press <strong>Okay</strong> to delete.');
		okFunction.set(async () => {
			const response = await fetch(PUBLIC_API_URL_DELETE, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: `Bearer ${$JWT}`
				},
				body: JSON.stringify({
					shorten: url.shortUrl
				})
			});
			if (!response.ok) {
				alert('something went wrong. please try again');
				return;
			}
			listOfUrls.update((currentUrls) => {
				// Filter out the deleted URL from the current value
				const updatedUrls = currentUrls.filter((curl) => curl.shortUrl !== url.shortUrl);
				return updatedUrls;
			});
		});
		showPopUp.set(true);
	};

	/**
	 * By default it's `Delete`
	 */
	export let deleteText: string = 'Delete';

	const visitUrl = () => {
		window.open(shortURL, '_blank');
	};

	const showQR = async () => {
		try {
			const image = await QRCode.toDataURL(shortURL);
			heading.set('Qr Code');
			content.set(`<img style="padding: 0.5rem 2rem;" src="${image}" alt="QR Code"/>`);
			okFunction.set(() => {
				const dataUrl = image;
				const downloadLink = document.createElement('a');
				downloadLink.href = dataUrl;
				downloadLink.download = 'qr.png';
				document.body.appendChild(downloadLink);
				downloadLink.click();
				document.body.removeChild(downloadLink);
			});
			okText.set('Download');
			showPopUp.set(true);
		} catch (err) {
			heading.set('Qr Code');
			content.set(`Something went wrong.`);
			showPopUp.set(true);
		}
	};

	const copyUrl = async () => {
		await navigator.clipboard.writeText(shortURL);
	};

	const deleteUrl = () => {
		onDeleteFunction();
	};
</script>

<div class="url-details" style="background-color: {url.expiresOn < new Date() ? "#fac3c3" : "none"};">
	<div class="short-link">
		<img src={zipitLogo} alt="Zipit" />
		<a href={shortURL} target="_blank">{trimLongString(shortURL, 30)}</a>
	</div>
	<div class="original-link" style="max-width: 100%;">
		<a href={url.originalUrl} target="_blank">{trimLongString(url.originalUrl, 40)}</a>
	</div>
	<div class="summery">
		<p>{formatDateTime(url.createdOn)}</p>
		<span class="vs"></span>
		<p>{formatDateTime(url.expiresOn)}</p>
	</div>
	<div class="actions">
		<button class="default-button" on:click={visitUrl}>Visit URL</button>
		<button class="default-button" on:click={showQR}>QR</button>
		<button class="default-button" on:click={copyUrl}>Copy</button>
		<button class="default-button delete-button" on:click={deleteUrl}>{deleteText}</button>
	</div>
</div>

<style>
	.url-details {
		background-color: #eef7fe;
		padding: 1rem;
		border-radius: 1rem;
		box-shadow: 0rem 0.2rem 0.5rem #52587d;
	}

	.short-link {
		display: flex;
		align-items: center;
	}
	.short-link img {
		width: 2rem;
		margin-right: 0.5rem;
	}
	.short-link a {
		font-size: 1rem;
		font-weight: 500;
	}
	.original-link a {
		font-size: 0.8rem;
		color: green;
	}
	.summery {
		display: flex;
		align-items: center;
	}
	.vs {
		width: 2px;
		height: 1.5rem;
		color: #52587d;
		background-color: #52587d;
		margin: 0px 1rem;
	}

	.default-button {
		background-color: #2162e0;
		color: #fff;
		border: none;
		border-radius: 0.5rem;
		padding: 0.5rem;
		cursor: pointer;
		transition: 0.3s;
	}
	.default-button:hover {
		box-shadow: 0 0.2rem 0.5rem #2162e0;
	}

	.default-button:active {
		box-shadow: none;
	}
	.delete-button {
		background-color: #e83366;
	}
	.delete-button:hover {
		box-shadow: 0 0.2rem 0.5rem #e83366;
	}
	.delete-button:active {
		box-shadow: none;
	}
</style>
