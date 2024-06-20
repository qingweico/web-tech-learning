export default function GetEnvVars() {
    const apiUrl = process.env.NEXT_PUBLIC_API_URL;
    const certificate = process.env.NEXT_PUBLIC_CERTIFICATE;
    return (
        <>
            <p>apiUrl; {apiUrl}</p>
            <p>certificate; {certificate}</p>
        </>


    )
}
