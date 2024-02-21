<script lang="ts">
	import { showPopUp, heading, content, okFunction, okText } from '$lib/store';

	function onClickOk() {
		$okFunction();
		showPopUp.set(false);
		okText.set('Okay');
	}
</script>

<div class="overlay" on:click={() => showPopUp.set(false)}>
	<div class="popup" on:click={(e) => e.stopPropagation()}>
		<div class="heading">{@html $heading}</div>
		<div class="content">{@html $content}</div>
		<div class="actions">
			<button class="cancel default-button" on:click={() => showPopUp.set(false)}>Cancel</button>
			<button class="action default-button delete-button" on:click={onClickOk}
				>{@html $okText}</button
			>
		</div>
	</div>
</div>

<style>
	.overlay {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 1000;
	}

	.popup {
		background: white;
		padding: 20px;
		border-radius: 5px;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
		text-align: center;
		position: fixed;
		margin: 0px;
		max-width: 70%;
	}
	.popup > * {
		margin: 0.5rem 0px;
	}
	.heading {
		font-size: 1.5rem;
		font-weight: bold;
	}
	.actions {
		display: flex;
		justify-content: space-around;
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
