import path from 'path';
import fsp from 'fs/promises';
import {NextRequest} from 'next/server';
import {logger} from '@/app/lib/logger/server';
import ResponseHelper from '@/app/api/ResponseHelper';

export async function GET(request: NextRequest) {
    const searchParams = request.nextUrl.searchParams;
    const dataPath = searchParams.get('path');
    const locale = searchParams.get('locale');
    if (dataPath === null || !dataPath) return ResponseHelper.badRequest('path is empty');
    if (locale === null || !locale) return ResponseHelper.badRequest('locale is empty');

    const dataDir = path.dirname(dataPath);
    const baseName = path.basename(dataPath);
    const filePath = path.join(process.cwd(), 'public', 'locales', locale, dataDir, `${baseName}.json`);

    try {
        const fileData = await fsp.readFile(filePath, "utf-8");
        return ResponseHelper.ok('OK', JSON.parse(fileData));
    } catch (e) {
        logger.error(e);
        return ResponseHelper.internalServerError(`Failed to open locale file: ${locale}/${dataPath}`);
    }
}
