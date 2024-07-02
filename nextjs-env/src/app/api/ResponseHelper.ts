import {NextResponse} from 'next/server';

export default class ResponseHelper {
    protected static success<JsonData>(status: number, msg: string, data: JsonData) {
        return NextResponse.json({code: 0, msg, data}, {status});
    }

    protected static failure<JsonData>(status: number, code: number, msg: string, data: JsonData) {
        return NextResponse.json({code, msg, data}, {status});
    }

    public static ok<JsonData>(msg: string, data: JsonData | null = null) {
        return this.success(200, msg, data);
    }

    public static badRequest(msg: string) {
        return this.failure(400, 400, msg, null);
    }

    public static internalServerError(msg: string) {
        return this.failure(500, 500, msg, null);
    }
}
