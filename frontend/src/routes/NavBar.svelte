<script lang="ts">
	import profile from '$lib/images/icons/animated/profile.json';
	import logout from '$lib/images/icons/animated/logout.json';
	import login from '$lib/images/icons/animated/login.json';
	import AnimatedButton from './AnimatedButton.svelte';
	import { goto } from '$app/navigation';
	import { onMount } from 'svelte';
	import { JWT, content, heading, isLoggedIn, showPopUp } from '$lib/store';
	import { PUBLIC_API_USER_HELLO } from '$env/static/public';

	onMount(async () => {
		if ($JWT.length === 0) return;

		// Lets try to connect to backend
		try {
			/**
			 * !TODO: Make a reusable method
			 */
			const response = await fetch(PUBLIC_API_USER_HELLO, {
				headers: {
					Authorization: `Bearer ${$JWT}`
				}
			});
			if (!response.ok) {
				heading.set('Error');
				content.set('Something went wrong. Please check your internet connection');
				showPopUp.set(true);
				return;
			}
			isLoggedIn.set(true);
		} catch (e) {
			heading.set('Error');
			content.set('Something went wrong. Please check your internet connection');
			showPopUp.set(true);
			console.log(e);
		}
	});

	const handleLogout = () => {
		JWT.set('');
		isLoggedIn.set(false);
		goto('/login');
	};
</script>

<nav>
	<a href="/">
		<div class="logo">
			<svg
				class="zipit-logo"
				width="54"
				height="50"
				viewBox="0 0 54 50"
				fill="none"
				xmlns="http://www.w3.org/2000/svg"
			>
				<path
					d="M28.9999 31.1909L33.7288 23.538C36.3405 19.3115 34.5498 13.7679 29.7291 11.1562C24.9084 8.54448 18.8832 9.85358 16.2715 14.0802L4.44918 33.2124C1.83746 37.4389 3.62818 42.9825 8.44888 45.5942C10.6626 46.7935 13.1302 47.166 15.4047 46.809C16.8006 46.5898 18.1237 46.0959 19.2706 45.3496M25.0002 18.8091L20.2712 26.462C17.6595 30.6885 19.4502 36.2321 24.2709 38.8438C29.0916 41.4555 35.1168 40.1464 37.7285 35.9199L49.5509 16.7876C52.1626 12.5611 50.3719 7.01754 45.5512 4.40582C43.3375 3.2065 40.8698 2.83397 38.5953 3.19105C37.1995 3.41019 35.8764 3.90412 34.7294 4.65039"
					stroke="url(#paint0_linear_7_14)"
					stroke-width="6"
					stroke-linecap="round"
				/>
				<defs>
					<linearGradient
						id="paint0_linear_7_14"
						x1="33.4705"
						y1="0.851857"
						x2="20.5295"
						y2="49.1482"
						gradientUnits="userSpaceOnUse"
					>
						<stop stop-color="#2162E0" />
						<stop offset="1" stop-color="#FF0000" />
					</linearGradient>
				</defs>
			</svg>
			<span>Zipit</span>
		</div>
	</a>
	<div class="actions">
		{#if $isLoggedIn}
			<AnimatedButton
				title="Profile"
				animationData={profile}
				buttonCss="--bg:none; --shadow:#52587d"
				spanCss="font-weight:700"
				onclick={() => goto('/profile')}
			/>
			<AnimatedButton
				title="Logout"
				animationData={logout}
				buttonCss="--bg:#e83366; --shadow:#e83366"
				spanCss="color:#FFF;font-weight:500; margin:0px"
				onclick={handleLogout}
			/>
		{:else}
			<AnimatedButton
				title="Login"
				animationData={login}
				buttonCss="--bg:#e83366; --shadow:#e83366"
				spanCss="color:#FFF;font-weight:500"
				onclick={() => goto('/login')}
			/>
		{/if}
	</div>
</nav>

<style>
	nav {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin: 1rem 0px;
	}

	nav .logo {
		display: flex;
		align-items: center;
	}

	nav .logo .zipit-logo {
		width: 2.5rem;
		height: 2.5rem;
	}

	nav .logo span {
		font-size: 1.5rem;
		margin-left: 1rem;
		font-weight: bold;
	}

	nav .actions {
		display: flex;
	}
</style>
