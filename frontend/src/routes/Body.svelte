<script lang="ts">
	import AnimatedScene from './AnimatedScene.svelte';
	import ToggleSwitch from './ToggleSwitch.svelte';
	import scene from '$lib/images/scene.svg';
	import NewUrlForm from './NewUrlForm.svelte';
	import { MONITOR_CLIPBOARD, showPopUp } from '$lib/store';
	import { UrlModel } from './Models';
	import ShortUrlCard from './ShortURLCard.svelte';

	let url: UrlModel;
	let showForm: boolean = true;
</script>

<div class="content">
	<AnimatedScene src={scene} />
	<div class="zipit-content">
		<div class="link-shortening">Easy Link Shortening</div>
		<div class="zipit-heading">Zipit short URL & QR code generator</div>
		<div class="zipit-subtitle">
			A short link allows you to collect so much about your customers & their behavior
		</div>
		<div class="clipboard">
			<ToggleSwitch bind:isEnabled={$MONITOR_CLIPBOARD} />
			<span class="button-label"> Monitor Clipboard</span>
		</div>
		{#if showForm}
			<NewUrlForm bind:showForm bind:url />
		{:else}
			<ShortUrlCard
				{url}
				deleteText="Make Another"
				onDeleteFunction={() => {
					showForm = true;
				}}
			/>
		{/if}
	</div>
</div>

<style>
	.content {
		padding: 0px;
		display: flex;
		align-items: center;
		flex-direction: row-reverse;
		justify-content: space-around;
		margin: auto 0px;
	}

	.content .zipit-content {
		max-width: 30vw;
		background-image: url('/src/lib/images/icons/static/arrow.svg');
		background-repeat: no-repeat;
		background-position: 0% 60%;
	}

	.content .link-shortening {
		background-color: rgb(232, 51, 102, 0.25);
		border-radius: 1rem;
		width: 12rem;
		text-align: center;
		padding: 0.3rem;
		color: var(--red);
	}

	.content .zipit-heading {
		margin-top: 1.5rem;
		font-size: 2rem;
		font-weight: bold;
	}

	.content .zipit-subtitle {
		margin-top: 1.5rem;
	}

	.content .clipboard {
		width: 100%;
		margin: 2rem 0px;
		text-align: center;
		justify-content: center;
		display: flex;
		align-items: center;
	}

	.button-label {
		font-weight: 500;
		font-size: 0.8rem;
		margin-left: 1rem;
	}

	@media only screen and (max-width: 600px) {
		.content {
			flex-direction: column;
			margin: 1rem 0px;
		}
		.content .zipit-content {
			max-width: 90vw;
		}
	}
</style>
