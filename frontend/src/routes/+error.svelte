<script lang="ts">
	import src from '$lib/images/404.svg';
</script>

<svelte:head>
	<title>Zipit | Not Found</title>
</svelte:head>

<h1>Page Not Found</h1>
<img {src} alt="404 Page" />

<style>
	h1 {
		text-align: center;
		font-size: 3rem;
		font-weight: bold;
		margin-top: 1rem;
	}
	img {
		height: 50vh;
	}
</style>
