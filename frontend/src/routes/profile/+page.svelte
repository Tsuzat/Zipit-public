<script lang="ts">
	import AnimatedScene from '../AnimatedScene.svelte';
	import src from '$lib/images/profile-scene.svg';
	import emailIcon from '$lib/images/icons/static/email.svg';
	import calenderIcon from '$lib/images/icons/static/calender.svg';
	import personIcon from '$lib/images/icons/static/person.svg';
	import passwordIcon from '$lib/images/icons/static/password.svg';
	import ShortUrlCard from '../ShortURLCard.svelte';
	import { onMount } from 'svelte';
	import {
		PUBLIC_API_USER_GET_USER,
		PUBLIC_API_URL_GET_ALL_URLS,
		PUBLIC_API_AUTH_CHANGE_PASSWORD_REQUEST,
		PUBLIC_API_USER_DELETE_USER
	} from '$env/static/public';
	import {
		JWT,
		listOfUrls,
		heading,
		content,
		showPopUp,
		isLoggedIn,
		okFunction,
		okText
	} from '$lib/store';
	import { UrlModel } from '../Models';
	import { goto } from '$app/navigation';
	import { formatDateTime } from '../Utilities';

	let firstName: string = '';
	let lastName: string = '';
	let email: string = '';
	let date: string = new Date().getDate().toString();
	let maxUrls: number = 0;

	$: fullName = `${firstName} ${lastName}`;

	onMount(async () => {
		try {
			let response = await fetch(PUBLIC_API_USER_GET_USER, {
				method: 'GET',
				headers: {
					Authorization: `Bearer ${$JWT}`
				}
			});
			if (!response.ok) {
				heading.set('Error');
				content.set('Something went wrong. Please check your internet connection. Or Login Again');
				showPopUp.set(true);
				goto('/');
				return;
			}
			let data = await response.json();
			firstName = data.firstName;
			lastName = data.lastName;
			email = data.email;
			date = data.createdOn;
			maxUrls = data.maxUrls;

			response = await fetch(PUBLIC_API_URL_GET_ALL_URLS, {
				method: 'GET',
				headers: {
					Authorization: `Bearer ${$JWT}`
				}
			});

			if (!response.ok) {
				heading.set('Error');
				content.set('Something went wrong. Please check your internet connection. Or Login Again');
				showPopUp.set(true);
				goto('/');
				return;
			}
			data = await response.json();
			let tmpUrls: UrlModel[] = [];
			data.forEach((element: any) => {
				tmpUrls.push(UrlModel.fromJSON(element));
			});
			listOfUrls.set(tmpUrls);
		} catch (e) {
			heading.set('Error');
			content.set('Something went wrong. Please check your internet connection');
			showPopUp.set(true);
		}
	});

	const deleteUser = async () => {
		heading.set('Dangerous');
		content.set('<strong>On deleteing the account, all the urls will also be deleted.</strong>');
		okText.set('Delete');
		okFunction.set(async () => {
			try {
				const response = await fetch(PUBLIC_API_USER_DELETE_USER, {
					method: 'delete',
					headers: {
						Authorization: `Bearer ${$JWT}`
					}
				});
				if (response.ok) {
					heading.set('Successfull');
					content.set(`We'll miss you. For now, Your account is deleted successfully`);
					okText.set('OK');
					okFunction.set(() => {});
					showPopUp.set(true);
					goto('/');
				} else {
					heading.set('Error');
					content.set('Something went wrong. Please try later');
					okText.set('OK');
					okFunction.set(() => {});
					showPopUp.set(true);
					goto('/');
				}
			} catch (e) {
				console.log(e);
				heading.set('Error');
				content.set('Something went wrong. Please try later');
				okText.set('OK');
				okFunction.set(() => {});
				showPopUp.set(true);
				goto('/');
			}
		});
		showPopUp.set(true);
	};

	const changePassword = async () => {
		try {
			const response = await fetch(PUBLIC_API_AUTH_CHANGE_PASSWORD_REQUEST, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					email: email
				})
			});
			if (!response.ok) {
				/**
				 * !TODO: This is a reminder that these three lines are being repeative
				 */
				heading.set('Error');
				content.set('Something went wrong. Please check your internet connection. Or Login Again');
				showPopUp.set(true);
				goto('/');
			}
			heading.set('Check Your Email');
			content.set(`We have send you an email for the password change.`);
			showPopUp.set(true);
			JWT.set('');
			isLoggedIn.set(false);
			goto('/');
		} catch (e) {
			heading.set('Error');
			content.set('Something went wrong. Please check your internet connection. Or Login Again');
			showPopUp.set(true);
		}
	};
</script>

<svelte:head>
	<title>Zipit | {fullName}</title>
	<meta
		name="description"
		content="Shorten and manage your URLs with Zipit, a fast and reliable URL shortener service."
	/>
</svelte:head>

<div class="content">
	<div class="animatedbg">
		<AnimatedScene {src} />
	</div>
	<div class="profile-details">
		<h1 class="heading">Welcome, {fullName}!!</h1>
		<div class="profile-card">
			<h1>Your Account Details</h1>
			<div class="details-grid">
				<div class="row">
					<div class="details">
						<img src={emailIcon} alt="Email" />
						<div>{email}</div>
					</div>
					<div class="details">
						<img src={calenderIcon} alt="Calender" />
						<div>{formatDateTime(new Date(date))}</div>
					</div>
				</div>
				<div class="row">
					<div class="details">
						<img src={personIcon} alt="Name" />
						<div>{fullName}</div>
					</div>
					<div class="details">
						<img src={passwordIcon} alt="Password" />
						<button class="default-button" on:click={changePassword}>Change Password</button>
					</div>
				</div>
			</div>
			<button class="default-button delete-account" on:click={deleteUser}>Delete My Account</button>
		</div>
	</div>
</div>

<h1 class="all-urls">Your URLs | Created : {$listOfUrls.length} / Limit: {maxUrls}</h1>
<div class="urls">
	{#if $listOfUrls.length > 0}
		{#each $listOfUrls as url}
			<ShortUrlCard {url} />
		{/each}
	{:else if $listOfUrls.length === 0}
		<h1>No Url Found</h1>
	{:else}
		<h1>Loading...</h1>
	{/if}
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

	.content .profile-details {
		min-width: 30vw;
		background-image: url('/src/lib/images/icons/static/arrow.svg');
		background-repeat: no-repeat;
		background-position: 0% 60%;
	}

	.content .profile-details .heading {
		font-size: 2rem;
	}

	.profile-card {
		background-color: #eef7fe;
		padding: 1rem;
		box-shadow: 0.2rem 0.2rem 0.5rem #52587d;
		border-radius: 1rem;
	}
	.profile-card > * {
		margin: 0.5rem 0px;
	}
	.details {
		display: flex;
		align-items: center;
	}

	.details > * {
		margin: 1rem 0.5rem;
	}

	.details img {
		width: 1.5rem;
	}

	.details div {
		background-color: #dbefff;
		padding: 0.5rem;
		border-radius: 0.5rem;
	}

	.details-grid {
		display: flex;
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
	.delete-account {
		background-color: #e83366;
	}
	.delete-account:hover {
		box-shadow: 0 0.2rem 0.5rem #e83366;
	}
	.delete-account:active {
		box-shadow: none;
	}

	.all-urls {
		margin-top: 1rem;
		font-size: 1.5rem;
		font-weight: 500;
	}

	.urls {
		margin-top: 1rem;
	}

	.urls {
		display: flex;
		flex-wrap: wrap;
		gap: 1rem;
		margin-left: auto;
		margin-right: auto;
	}
	@media only screen and (max-width: 660px) {
		.animatedbg {
			display: none;
		}
		.details-grid {
			display: block;
		}
		.content {
			margin: 1rem 0px;
		}
		.content .profile-details {
			max-width: 90%;
			background-image: url('/src/lib/images/icons/static/arrow.svg');
			background-repeat: no-repeat;
			background-position: 0% 60%;
		}
	}
</style>
