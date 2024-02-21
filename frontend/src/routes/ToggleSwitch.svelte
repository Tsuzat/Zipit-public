<script lang="ts">
	export let isEnabled: boolean = false;
	export let onClick: Function = () => {};

	$: toggleClass = `toggle ${isEnabled ? 'enable' : ''}`;

	function toggle() {
		isEnabled = !isEnabled;
		onClick();
	}
</script>

<button class={toggleClass} on:click={toggle}>
	<div class="circle"></div>
</button>

<style>
	.toggle {
		width: 3.6rem;
		height: 1.8rem;
		background-color: rgba(82, 88, 125, 0.3);
		border: 1px rgba(82, 88, 125, 0.5) solid;
		border-radius: 1.5rem;
		display: flex;
		align-items: center;
		box-shadow: 0 0 0.25rem #52587d;
		padding: 0px;
		transition: 0.3s;
	}

	.toggle:hover {
		box-shadow: 0 0 0.5rem #52587d;
	}

	.toggle .circle {
		position: relative;
		width: 1.5rem;
		height: 1.5rem;
		background-color: #52587d;
		border-radius: 1.5rem;
		margin-left: 0.1rem;
		transition: 0.3s;
	}

	.toggle:hover .circle {
		width: 1.8rem;
		border-radius: 1.5rem;
	}

	.enable {
		background-color: rgba(232, 51, 102, 0.3);
		border: 1px rgba(232, 51, 102, 0.5) solid;
		border-radius: 1.5rem;
		display: flex;
		align-items: center;
		box-shadow: 0 0 0.25rem #e83366;
	}

	.enable:hover {
		box-shadow: 0 0 0.5rem #e83366;
	}

	.enable .circle {
		background-color: #e83366;
		margin-right: 0.1rem;
		margin-left: auto;
	}
</style>
