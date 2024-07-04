import {NextRequest} from "next/server";
import ResponseHelper from "@/app/api/ResponseHelper";


export async function GET(request: NextRequest) {
    const searchParams = request.nextUrl.searchParams;
    const rid = searchParams.get('rid') ?? '';
    try {
        const res = await fetch('https://jsonplaceholder.typicode.com/users');
        const headerDate = res.headers?.get('date') ?? 'no response date';
        // console.log('Status Code:', res.status);
        // console.log('Date in Response header:', headerDate);

        const users = await res.json();

        //console.log(users);
        return ResponseHelper.ok(rid, users);
    } catch (e) {
        console.log(e)
        return ResponseHelper.internalServerError(`Failed to login\n${e}`);
    }
}
