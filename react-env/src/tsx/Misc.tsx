import {useState, useEffect} from "react";

interface ButtonProps {
    /** 按钮文字 */
    title: string;
    /** 按钮是否禁用 */
    disabled: boolean;
}

function CustomButton({title, disabled}: ButtonProps) {
    return <button disabled={disabled}>{title}</button>
}

export default function HelloWorld() {
    return (
        <div>
            <h1>HelloWorld</h1>
            <CustomButton disabled={true} title={"我是一个禁用按钮"}></CustomButton>
        </div>
    )
}

// 联合类型
// type State = "idle" | "loading" | "success" | "error";
type RequestState = | { status: 'idle' }
    | { status: 'loading' }
    | { status: 'success', data: any }
    | { status: 'error', error: Error };


function fetchData(url: string): Promise<any> {
    return new Promise((resolve, reject) => {
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setTimeout(() => {
                    resolve(data);
                }, 2000);
            })
            .catch(error => {
                setTimeout(() => {
                    reject(error);
                }, 2000);
            });
    });
}

function useRequest(url: string) {
    const [state, setState] = useState<RequestState>({status: 'idle'});
    useEffect(() => {
        setState({status: 'loading'});

        fetchData(url)
            .then(data => setState({status: 'success', data}))
            .catch(error => setState({status: 'error', error}));
    }, [url]);

    return state;
}

function Starter() {
    const state = useRequest('https://baidu.com');
    if (state.status === 'idle') {
        return <div>Idle...</div>;
    }

    if (state.status === 'loading') {
        return <div>Loading...</div>;
    }

    if (state.status === 'success') {
        return (
            <div>
                Success! Data: <pre>{JSON.stringify(state, null, 2)}</pre>
            </div>
        );
    }

    if (state.status === 'error') {
        return (
            <div>
                Error: {state.error.message}
            </div>
        );
    }

    return null;
}

export {Starter}

