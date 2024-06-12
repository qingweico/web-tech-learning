import {useState} from 'react';

export default function TaskList({tasks, onChangeTask, onDeleteTask}) {
    return <ul>
        {tasks.map(e => <li key={e.id}>
            <Task task={e} onChange={onChangeTask} onDelete={onDeleteTask}></Task>
        </li>)}
    </ul>
}


function Task({task, onChange, onDelete}) {
    const [editing, setEditing] = useState(false);
    let taskContent;
    if (editing) {
        taskContent = (<>
            <input value={task.text}
                   onChange={e => {
                       onChange({
                           ...task, text: e.target.value
                       })
                   }}/>
            <button onClick={() => setEditing(false)}>保存</button>
        </>)
    } else {
        taskContent = (<>
            {task.text}
            <button onClick={() => setEditing(true)}>编辑</button>
        </>)
    }

    return (<label>
            <input type='checkbox' checked={task.done}
                   onChange={e => {
                       onChange({
                           ...task, done: e.target.checked
                       })
                   }}/>
            {taskContent}
            <button onClick={() => onDelete(task.id)}>
                删除
            </button>
        </label>)

}
