import {useReducer} from 'react';
import AddTask from './AddTask.js';
import TaskList from './TaskList.js';

export default function TaskApp() {
    const [tasks, dispatch] = useReducer(tasksReducer, initialTasks);

    function handleAddTask(text) {
        dispatch({
            type: 'added', id: nextId++, text: text,
        });
    }

    function handleChangeTask(task) {
        dispatch({
            type: 'changed', task: task
        });
    }

    function handleDeleteTask(taskId) {
        dispatch({
            type: 'deleted', id: taskId
        });
    }

    return <>
        <h1>待办任务</h1>
        <AddTask onAddTask={handleAddTask}/>
        <TaskList tasks={tasks} onChangeTask={handleChangeTask} onDeleteTask={handleDeleteTask}/>
    </>


}

// reducer 函数接受两个参数, 一个是当前状态和一个动作 并返回新的状态
function tasksReducer(tasks, action) {
    switch (action.type) {
        case 'added': {
            return [...tasks, {
                id: action.id, text: action.text, done: false
            }];
        }
        case 'changed': {
            tasks.map((task) => {
                if (task.id === action.task.id) {
                    return action.task
                } else {
                    return task
                }
            })
        }
        case 'deleted': {
            return tasks.filter((task) => task.id !== action.task.id)
        }
        default: {
            throw new Error('Invalid action type: ' + action.type)
        }
    }
}

let nextId = 3;
const initialTasks = [
    {id: 0, text: '参观卡夫卡博物馆', done: true},
    {id: 1, text: '看木偶戏', done: false},
    {id: 2, text: '列侬墙图片', done: false}
];
