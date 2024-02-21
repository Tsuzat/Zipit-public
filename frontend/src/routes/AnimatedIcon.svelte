<script lang="ts">
	import { onMount } from 'svelte';

	export let animationData: {};
	export let loop: boolean = true;
	export let autoplay: boolean = true;
	export let animationCss: string = '';

	let lottieContainer: HTMLElement;
	onMount(async () => {
		const lottie = await import('lottie-web');
		const animation = lottie.loadAnimation({
			container: lottieContainer,
			animationData,
			render: 'svg',
			loop,
			autoplay
		});
	});
</script>

<div class="lottie" bind:this={lottieContainer} style={animationCss}></div>
