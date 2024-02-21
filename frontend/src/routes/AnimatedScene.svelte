<script lang="ts">
	export let src: string;
	export let style: string = '';

	let hoverDiv: HTMLElement;
	let tiltingImage: HTMLElement;

	const tiltImage = (event: MouseEvent) => {
		var divRect = hoverDiv.getBoundingClientRect();
		// Calculate the center of the div
		var centerX = divRect.left + divRect.width / 2;
		var centerY = divRect.top + divRect.height / 2;

		// Calculate the distance of the mouse from the center
		var distanceX = event.clientX - centerX;
		var distanceY = event.clientY - centerY;

		// Normalize the distances to be in the range of -10 to 10
		var normalizedX = (distanceX / (divRect.width / 2)) * 10;
		var normalizedY = (distanceY / (divRect.height / 2)) * 10;

		// Apply the tilt transformation to the image
		tiltingImage.style.transform =
			'perspective(1000px) rotateX(' + normalizedY + 'deg) rotateY(' + normalizedX + 'deg)';
	};
</script>

<div bind:this={hoverDiv} on:pointermove={tiltImage}>
	<img id="animated-scene" bind:this={tiltingImage} {src} alt="scene" {style} />
</div>
