<script lang="ts">
	import AnimatedScene from '../AnimatedScene.svelte';
	import src from '$lib/images/login-scene.svg';
	import emailIcon from '$lib/images/icons/static/email.svg';
	import passwordIcon from '$lib/images/icons/static/password.svg';
	import hideIcon from '$lib/images/icons/static/hide.svg';
	import unhideIcon from '$lib/images/icons/static/unhide.svg';
	import personIcon from '$lib/images/icons/static/person.svg';

	import { PUBLIC_API_AUTH_REGISTER } from '$env/static/public';
	import { content, heading, showPopUp } from '$lib/store';
	import { goto } from '$app/navigation';

	let email: string = '';
	let password: string = '';
	let confirmedPassword: string = '';
	let firstName: string = '';
	let lastName: string = '';

	let error: string = '';

	let isPasswordVisible: boolean = false;
	let passwordElement: HTMLInputElement;

	function toggleHide() {
		passwordElement.type = isPasswordVisible ? 'password' : 'text';
		isPasswordVisible = !isPasswordVisible;
	}

	function startOver() {
		email = '';
		password = '';
		confirmedPassword = '';
		firstName = '';
		lastName = '';
		error = '';
	}

	function checkError() {
		if (password.length === 0 && confirmedPassword.length === 0) {
			error = '';
			return;
		}
		if (password.length < 8) {
			error = 'Password should have at least 8 letters';
			return;
		}
		if (password !== confirmedPassword) {
			error = 'Passwords do not match';
			return;
		}
		error = '';
	}

	const registerUser = async () => {
		let data = {
			firstName: firstName,
			lastName: lastName,
			email: email,
			password: password
		};
		try {
			const response = await fetch(PUBLIC_API_AUTH_REGISTER, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(data)
			});
			if (!response.ok) {
				heading.set('error');
				content.set('something went wrong. please check your internet connection. or login again');
				showPopUp.set(true);
			} else {
				heading.set('Check Your email');
				content.set('Confirm your email for confirmation within <strong> 15 min </strong>');
				showPopUp.set(true);
			}
		} catch (e) {
			heading.set('error');
			content.set('something went wrong. please check your internet connection. or login again');
			showPopUp.set(true);
		} finally {
			goto('/login');
		}
	};
</script>

<svelte:head>
	<title>Zipit | SignUp</title>
	<meta
		name="description"
		content="Shorten and manage your URLs with Zipit, a fast and reliable URL shortener service."
	/>
</svelte:head>

<div class="content">
	<AnimatedScene {src} />
	<div class="login-content">
		<h1>Welcome to the Zipit!!</h1>
		<p>Please enter your details to <strong>Sign Up</strong></p>
		<form on:submit={registerUser}>
			<div class="input-field">
				<img src={personIcon} alt="Person" />
				<input class="input" bind:value={firstName} type="text" required placeholder="First Name" />
				<input class="input" bind:value={lastName} type="text" required placeholder="Last Name" />
			</div>
			<div class="input-field">
				<img src={emailIcon} alt="Email" />
				<input class="input" bind:value={email} type="email" required placeholder="Your Email" />
			</div>
			<div class="input-field">
				<img src={passwordIcon} alt="Password" />
				<input
					class="input"
					bind:value={password}
					on:blur={checkError}
					on:input={checkError}
					type="password"
					required
					placeholder="Your Password"
				/>
			</div>
			<div class="input-field">
				<img src={passwordIcon} alt="Password" />
				<div class="input">
					<input
						style="border: none; background:none"
						bind:value={confirmedPassword}
						bind:this={passwordElement}
						on:blur={checkError}
						on:input={checkError}
						type="password"
						required
						placeholder="Confirm Password"
					/>
					<button type="button" on:click={toggleHide}
						><img
							src={isPasswordVisible ? hideIcon : unhideIcon}
							alt="Unhide"
							style="width: 1rem;"
						/></button
					>
				</div>
			</div>
			<div class="error" style="color: {error.length > 0 ? `red` : `green`};">
				{error.length > 0 ? error : 'Looks all good!!!'}
			</div>
			<div class="submit">
				<div class="signup">Already a user? <a href="/login">Click Here</a></div>
				<button type="submit">Sign Up</button>
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
	input {
		width: 100%;
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
