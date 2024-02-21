<script lang="ts">
	import { onMount } from 'svelte';

	export let title: string = '';
	export let animationData: {};
	export let buttonCss: string = '';
	export let animationCss: string = '';
	export let spanCss: string = '';
	export let onclick: Function = () => {};

	const handleClick = () => onclick();

	let lottieContainer: HTMLElement;
	let button: HTMLElement;
	onMount(async () => {
		const lottie = await import('lottie-web');
		const animation = lottie.loadAnimation({
			container: lottieContainer,
			animationData,
			render: 'svg',
			loop: false,
			autoplay: false
		});

		var directionMenu = 1;
		button.addEventListener('mouseenter', (e) => {
			animation.setDirection(directionMenu);
			animation.play();
		});

		button.addEventListener('mouseleave', (e) => {
			animation.stop();
		});
		``;
	});
</script>

<button class="button" bind:this={button} style={buttonCss} on:click={handleClick}>
	<div class="lottie" bind:this={lottieContainer} style={animationCss}></div>
	<span style={spanCss}>{@html title}</span>
</button>

<style>
	.button {
		background-color: var(--bg);
		transition: box-shadow 0.3s;
	}
	.button:hover {
		box-shadow: 0 0.2rem 0.5rem var(--shadow);
	}

	.button .lottie {
		min-height: 1.5rem;
		min-width: 1.5rem;
	}
</style>
