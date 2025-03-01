export default class FetchError<BodyJson> extends Error {
    public url: string;
    public status: number;
    public info: BodyJson;

    constructor(url: string, status: number, info: BodyJson, message?: string, options?: ErrorOptions) {
        super(message, options);
        this.url = url;
        this.status = status;
        this.info = info;
    }

    public toString() {
        return `
            ${this.name}: ${this.message}
            URL: ${this.url}
            status: ${this.status}
            info: ${JSON.stringify(this.info)}
                `;
    }
}
