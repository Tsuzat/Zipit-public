<script lang="ts">
	import AnimatedScene from '../AnimatedScene.svelte';
	import src from '$lib/images/login-scene.svg';
	import emailIcon from '$lib/images/icons/static/email.svg';
	import passwordIcon from '$lib/images/icons/static/password.svg';
	import hideIcon from '$lib/images/icons/static/hide.svg';
	import unhideIcon from '$lib/images/icons/static/unhide.svg';

	import {
		PUBLIC_API_AUTH_AUTHENTICATE,
		PUBLIC_API_AUTH_CHANGE_PASSWORD_REQUEST
	} from '$env/static/public';
	import { JWT, content, heading, isLoggedIn, showPopUp } from '$lib/store';
	import { goto } from '$app/navigation';

	let email: string;
	let password: string;
	let submitForm: boolean = false;

	let isPasswordVisible: boolean = false;
	let passwordElement: HTMLInputElement;

	function toggleHide() {
		passwordElement.type = isPasswordVisible ? 'password' : 'text';
		isPasswordVisible = !isPasswordVisible;
	}

	const forgetPassword = async () => {
		const urlRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		// Check if clipboard content matches the URL format
		if (!email || email.length < 1 || !urlRegex.test(email)) {
			console.log('Email Invalid');
			heading.set('Invalid Email Field');
			content.set('Put email in login page and click forget button');
			showPopUp.set(true);
			return;
		}
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
			goto('/');
		} catch (e) {
			heading.set('Error');
			content.set('Something went wrong. Please check your internet connection. Or Login Again');
			showPopUp.set(true);
		}
	};

	const authenticate = async () => {
		submitForm = true;
		try {
			const response = await fetch(PUBLIC_API_AUTH_AUTHENTICATE, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					email: email,
					password: password
				})
			});
			if (!response.ok) {
				// That means forbidden
				if (response.status === 403) {
					heading.set('Login Failed');
					content.set('Please check your login credentials');
					showPopUp.set(true);
				}
				console.log('Server response = ', response.statusText);
				email = '';
				password = '';
				submitForm = false;
				return;
			}

			const data = await response.json();
			JWT.set(data.token);
			isLoggedIn.set(true);
			goto('/');
		} catch (e) {
			heading.set('Error');
			content.set('Something went wrong. Please check your internet connection');
			showPopUp.set(true);
		}
		email = '';
		password = '';
		submitForm = false;
	};
</script>

<svelte:head>
	<title>Zipit | Login</title>
	<meta
		name="description"
		content="Shorten and manage your URLs with Zipit, a fast and reliable URL shortener service."
	/>
</svelte:head>

<div class="content">
	<AnimatedScene {src} />
	<div class="login-content">
		<h1>Welcome, Back!!</h1>
		<p>Please enter your details to <strong>Log In</strong></p>
		<form on:submit={authenticate}>
			<div class="input-field">
				<img src={emailIcon} alt="Email" />
				<input class="input" bind:value={email} type="email" required placeholder="Your Email" />
			</div>
			<div class="input-field">
				<img src={passwordIcon} alt="Password" />
				<div class="input">
					<input
						style="border: none; background:none"
						bind:value={password}
						bind:this={passwordElement}
						type="password"
						required
						placeholder="Your Password"
					/>
					<button type="button" style="margin-left: auto;" on:click={toggleHide}
						><img
							src={isPasswordVisible ? hideIcon : unhideIcon}
							alt="Unhide"
							style="width: 1rem;"
						/></button
					>
				</div>
			</div>
			<div class="forgot-password">
				<button type="button" on:click={forgetPassword}>Forgot Password?</button>
			</div>
			<div class="submit">
				<div class="signup">Not a user? <a href="/signup">Click Here</a></div>
				<button type="submit">{submitForm ? 'Logging In' : 'Log In'}</button>
			</div>
		</form>
	</div>
</div>

<style>
	.content {
		padding: 0px;
		display: flex;
		align-items: center;
		flex-direction: row-reverse;
		justify-content: space-around;
		margin: auto 1rem;
	}
	.content .login-content {
		min-width: 30vw;
		background-image: url('/src/lib/images/icons/static/arrow.svg');
		background-repeat: no-repeat;
		background-position: 0% 60%;
	}
	.content .login-content h1 {
		font-size: 2rem;
		font-weight: bold;
	}

	.content .login-content form {
		padding: 1rem;
		background-color: #eef7fe;
		border-radius: 1rem;
	}
	.content .login-content form > * {
		margin: 1rem 0px;
	}

	.content .login-content form div {
		display: flex;
	}
	.content .login-content form div img {
		width: 1.5rem;
	}
	.content .login-content form div .input {
		width: 100%;
		border: none;
		background: none;
		margin-left: 1rem;
		background-color: #dbefff;
		padding: 0.5rem;
		border-radius: 0.5rem;
	}
	button {
		background: none;
		border: none;
		cursor: pointer;
	}
	.content .login-content form .forgot-password button {
		color: #e83366;
		margin-top: 0px;
		margin-left: auto;
	}
	.content .login-content form .submit {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	.content .login-content form .submit .signup a {
		color: #e83366;
		margin-left: 0.5rem;
	}
	.content .login-content form .submit button {
		background-color: #e83366;
		color: #fff;
		padding: 0.5rem 1rem;
		border-radius: 2rem;
	}
	@media only screen and (max-width: 600px) {
		.content {
			flex-direction: column;
			margin: 1rem 0px;
		}

		.content .login-content {
			min-width: 90vw;
		}
	}
</style>
