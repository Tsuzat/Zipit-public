export class UrlModel {
	shortUrl: string;
	originalUrl: string;
	createdOn: Date;
	expiresOn: Date;

	static fromJSON(data: any): UrlModel {
		return new UrlModel(
			data.shorten,
			data.original,
			new Date(data.createdOn),
			new Date(data.expiresOn)
		);
	}

	constructor(shortUrl: string, originalUrl: string, createdOn: Date, expiresOn: Date) {
		this.shortUrl = shortUrl;
		this.originalUrl = originalUrl;
		this.createdOn = createdOn;
		this.expiresOn = expiresOn;
	}
}
