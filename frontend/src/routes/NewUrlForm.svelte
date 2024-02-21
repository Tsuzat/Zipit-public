<script lang="ts">
	import urlIcon from '$lib/images/icons/static/link.svg';
	import aliasIcon from '$lib/images/icons/static/alias-link.svg';
	import processingIcon from '$lib/images/icons/animated/processing.json';
	import AnimatedIcon from './AnimatedIcon.svelte';
	import checkIcon from '$lib/images/icons/static/check.svg';
	import erorrIcon from '$lib/images/icons/static/error.svg';

	import { PUBLIC_API_URL_CHECK_ALIAS, PUBLIC_API_URL_ADD } from '$env/static/public';
	import {
		JWT,
		MONITOR_CLIPBOARD,
		content,
		heading,
		okFunction,
		okText,
		showPopUp
	} from '$lib/store';
	import { onMount } from 'svelte';
	import { UrlModel } from './Models';
	import { goto } from '$app/navigation';

	enum FormStatus {
		OK = 'OK',
		PROCESSING = 'PROCESSING',
		ERROR = 'ERROR'
	}

	export let url: UrlModel;
	export let showForm: boolean;

	let original: string = '';
	let alias: string = '';

	let statusText = 'Please Provide the details';

	let status: FormStatus = FormStatus.PROCESSING;

	let statusColor = '#52587d';

	$: {
		if (status === FormStatus.OK) statusColor = 'green';
		else if (status === FormStatus.ERROR) statusColor = '#e8336';
		else statusColor = '#52587d';
	}

	async function processAlias() {
		if (alias.length === 0) {
			status = FormStatus.OK;
			statusText = 'All Good';
			return;
		}
		status = FormStatus.PROCESSING;
		statusText = 'Processing';
		let data = { alias: alias };
		const response = await fetch(PUBLIC_API_URL_CHECK_ALIAS, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				Authorization: `Bearer ${$JWT}`
			},
			body: JSON.stringify(data)
		});

		if (response.status === 202) {
			status = FormStatus.OK;
			statusText = 'Alias is availabe';
		} else if (response.status === 403) {
			status = FormStatus.ERROR;
			statusText = 'Access Denied. Please Login';
			heading.set('Error');
			content.set('Access Denied, Please Login');
			okText.set('Login');
			okFunction.set(() => goto('/login'));
			showPopUp.set(true);
		} else {
			status = FormStatus.ERROR;
			statusText = 'Alias is already taken';
		}
	}

	onMount(async () => {
		if (!$MONITOR_CLIPBOARD) return;
		const clipboard = await navigator.clipboard.readText();
		const urlRegex = /^(ftp|http|https):\/\/[^ "]+$/;
		if (urlRegex.test(clipboard)) {
			original = clipboard;
			status = FormStatus.OK;
			statusText = 'All Good';
		}
	});

	const shortUrl = async () => {
		let data = {
			original: original,
			alias: alias
		};
		try {
			const response = await fetch(PUBLIC_API_URL_ADD, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: `Bearer ${$JWT}`
				},
				body: JSON.stringify(data)
			});
			if (response.status === 403) {
				heading.set('Error');
				content.set('Access Denied, Please Login');
				okText.set('Login');
				okFunction.set(() => goto('/login'));
				showPopUp.set(true);
			} else if (response.status === 400) {
				heading.set('Url Limit Reaches');
				content.set('Delete old urls to create new.');
				okText.set('See More');
				okFunction.set(() => goto('/profile'));
				showPopUp.set(true);
			} else if (!response.ok) {
				heading.set('Error');
				content.set('Something went wrong. Please try later');
				showPopUp.set(true);
			} else {
				const res = await response.json();
				url = UrlModel.fromJSON(res);
				showForm = false;
			}
		} catch (e) {
			heading.set('Error');
			content.set('Something went wrong. Please try later');
			showPopUp.set(true);
		}
	};
</script>

<form on:submit={shortUrl}>
	<div class="form">
		<div class="input-field">
			<img class="icon" src={urlIcon} alt="Url Icon" />
			<input type="url" name="url" bind:value={original} placeholder="*Paste Your Url" required />
		</div>
		<div class="input-field">
			<img class="icon" src={aliasIcon} alt="Alias Icon" />
			<input
				type="text"
				name="alias"
				bind:value={alias}
				on:blur={processAlias}
				placeholder="Your Alias"
			/>
		</div>
		<div class="submit">
			{#if status === FormStatus.OK}
				<img src={checkIcon} alt="Ok" />
			{:else if status === FormStatus.ERROR}
				<img src={erorrIcon} alt="Error" />
			{:else}
				<AnimatedIcon animationData={processingIcon} animationCss="height:1.5rem" />
			{/if}
			<div class="status" style="color: {statusColor};">{statusText}</div>
			<button type="submit">Shorten</button>
			<!-- <button> <AnimatedIcon animationData={loadingIcon} /> </button> -->
		</div>
	</div>
</form>

<style>
	.form {
		background-color: rgba(238, 247, 254, 0.8);
		padding: 1rem;
		border-radius: 1rem;
	}
	.form > * {
		margin: 1rem 0;
	}
	img {
		width: 1.2rem;
	}

	.form div {
		display: flex;
		align-items: center;
		justify-content: start;
	}
	input {
		border: none;
		border-bottom: 0px solid black;
		margin: 0px 1rem;
		width: 100%;
		transition: 0.3s;
		background: none;
	}
	.submit {
		display: flex;
		align-items: center;
	}
	.submit div {
		margin-left: 1rem;
		font-size: 0.8rem;
	}
	.submit button {
		margin-left: auto;
		width: 5rem;
		height: 2.5rem;
		background-color: #2162e0;
		color: #fff;
		padding: 0.5rem 0.75rem;
		border: none;
		border-radius: 2rem;
		cursor: pointer;
		transition: box-shadow 0.3s;
	}
	.submit button:hover {
		box-shadow: 0 0.2rem 0.5rem #2162e0;
	}
</style>
