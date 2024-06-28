import {NextResponse, NextRequest} from 'next/server'

export async function GET(request: NextRequest) {
    const isAuthenticated = authenticate(request)

    // If the user is authenticated, continue as normal
    if (isAuthenticated) {
        return NextResponse.next()
    }

    // Redirect to login page if not authenticated
    return NextResponse.redirect(new URL('/login', request.url))
}

function authenticate(request: NextRequest) {
    return false
}
