import clsx from 'clsx';
// 使用clsx库切换类名
export default function InvoiceStatus({status}: { status: string }) {
    return (
        <span
            className={clsx(
                'inline-flex items-center rounded-full px-2 py-1 text-sm',
                {
                    'bg-gray-100 text-gray-500': status === 'pending',
                    'bg-green-500 text-white': status === 'paid',
                },
            )}
        > {status === 'pending' ? '待支付' : '已支付'}
        </span>

    )
}
