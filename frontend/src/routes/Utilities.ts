export function formatDateTime(dateTime: Date): string {
	const now = new Date();
	const diff = Math.abs(now.getTime() - dateTime.getTime()); // Get absolute difference in milliseconds

	const years = Math.floor(diff / (1000 * 60 * 60 * 24 * 365));
	const months = Math.floor(diff / (1000 * 60 * 60 * 24 * 30)) % 12;
	const days = Math.floor(diff / (1000 * 60 * 60 * 24)) % 30;
	const hours = Math.floor(diff / (1000 * 60 * 60)) % 24;
	const minutes = Math.floor(diff / (1000 * 60)) % 60;
	const seconds = Math.floor(diff / 1000) % 60;

	const units = [
		{ value: years, unit: 'year' },
		{ value: months, unit: 'month' },
		{ value: days, unit: 'day' },
		{ value: hours, unit: 'hour' },
		{ value: minutes, unit: 'minute' },
		{ value: seconds, unit: 'second' }
	];

	const [topUnit] = units.filter((u) => u.value > 0); // Get the first non-zero unit

	if (topUnit && topUnit.value > 0) {
		const formatted = `${topUnit.value} ${topUnit.value === 1 ? topUnit.unit : topUnit.unit + 's'}`;

		return dateTime < now ? `${formatted} ago` : `in ${formatted}`;
	}
	return 'Just Now';
}

/**
 * Trims the long string into max 40 character string
 * @param url string
 * @returns string
 */
export const trimLongString = (url: string, max_length: number = 40): string => {
	if (url.length <= max_length) return url;
	return url.substring(0, max_length - 3) + '...';
};
