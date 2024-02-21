<script>
	import { showPopUp } from '$lib/store';
	import GlobalPopUp from './GlobalPopUp.svelte';
	import NavBar from './NavBar.svelte';
	import './styles.css';
</script>

<svelte:head>
	<title>Zipit | URL Shortener</title>
	<meta
		name="description"
		content="Shorten and manage your URLs with Zipit, a fast and reliable URL shortener service."
	/>
</svelte:head>

<div class="app">
	<NavBar />

	{#if $showPopUp}
		<GlobalPopUp />
	{/if}

	<main>
		<slot />
	</main>

	<footer>
		<p>
			Make with ❤️ by <a href="https://github.com/Tsuzat" target="_blank">Tsuzat</a> to learn SvelteKit
		</p>
	</footer>
</div>

<style>
	.app {
		display: flex;
		flex-direction: column;
		min-height: 100vh;
	}

	main {
		flex: 1;
		display: flex;
		flex-direction: column;
		padding: 1rem;
		width: 100%;
		max-width: 64rem;
		margin: 0 auto;
		box-sizing: border-box;
	}

	footer {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		padding: 12px;
	}

	footer a {
		font-weight: bold;
	}

	@media (min-width: 480px) {
		footer {
			padding: 12px 0;
		}
	}
</style>
