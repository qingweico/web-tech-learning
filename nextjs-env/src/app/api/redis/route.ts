import {NextResponse} from 'next/server';
import Redis from 'ioredis';

const redisUrl = process.env.REDIS_URL;

if (!redisUrl) {
    throw new Error('REDIS_URL is not defined in environment variables');
}
const redis = new Redis(redisUrl);

interface RequestBody {
    key: string;
    value: string;
}

export async function GET() {
    try {
        const value = await redis.get('Next-Redis-Key');
        return NextResponse.json({value});
    } catch (error: any) {
        return NextResponse.json({error: error.message}, {status: 500});
    }
}

export async function POST(req: Request) {
    try {
        const body: RequestBody = await req.json();
        const {key, value} = body;
        await redis.set(key, value);
        return NextResponse.json({message: 'Key set successfully'});
    } catch (error: any) {
        return NextResponse.json({error: error.message}, {status: 500});
    }
}
