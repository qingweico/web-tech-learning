// state 管理
import {useState} from 'react';

export default function Answer() {
    const [answer, setAnswer] = useState('');
    const [error, setError] = useState(null);
    const [status, setStatus] = useState('typing');
    if (status === 'success') {
        return <h1>答对了</h1>
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setStatus('submitted');
        try {
            await submitForm(answer);
            setStatus("success")
        } catch (err) {
            setAnswer('')
            setStatus("typing")
            setError(err);
        }
    }

    function handleTextareaChange(e) {
        setAnswer(e.target.value);
    }

    function submitForm(answer) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                let shouldError = answer.toLowerCase() !== '2'
                if (shouldError) {
                    reject(new Error('猜的不错, 但答案不对, 再试试看吧!'));
                } else {
                    resolve();
                }
            }, 1500);
        });
    }

    return (<>
        <form onSubmit={handleSubmit}>
                <textarea value={answer}
                          onChange={handleTextareaChange}
                          placeholder={'1 + 1等于几'}
                          disabled={status === 'submitted'}
                />
            <br/>
            <button disabled={answer.length === 0 ||
                status === 'submitted'}>提交</button>
            {error !== null &&
                <p className="Error">
                    {error.message}
                </p>
            }
        </form>
    </>)

}
