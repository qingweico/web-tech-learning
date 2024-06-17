// 井字棋游戏
import {useState} from 'react';

function Square({value, onSquareClick}) {
    return <button className="square" onClick={onSquareClick}>{value}</button>;
}

function Board({turnX, squares, onPlay}) {
    function handleClick(i) {
        if (squares[i] || calculateWinner(squares)) {
            return;
        }
        // slice() 会创建 squares 副本
        const nextSquares = squares.slice();
        if (turnX) {
            nextSquares[i] = 'X';
        } else {
            nextSquares[i] = 'O';
        }
        onPlay(nextSquares);
    }

    const winner = calculateWinner(squares);
    let status;
    if (winner) {
        status = "Winner: " + winner;
    } else {
        status = "Next player: " + (turnX ? "X" : "O");
    }

    return (
        <>
            <div className="status">{status}</div>
            <div className="board-row">
                <Square value={squares[0]} onSquareClick={() => handleClick(0)}/>
                <Square value={squares[1]} onSquareClick={() => handleClick(1)}/>
                <Square value={squares[2]} onSquareClick={() => handleClick(2)}/>
            </div>
            <div className="board-row">
                <Square value={squares[3]} onSquareClick={() => handleClick(3)}/>
                <Square value={squares[4]} onSquareClick={() => handleClick(4)}/>
                <Square value={squares[5]} onSquareClick={() => handleClick(5)}/>
            </div>
            <div className="board-row">
                <Square value={squares[6]} onSquareClick={() => handleClick(6)}/>
                <Square value={squares[7]} onSquareClick={() => handleClick(7)}/>
                <Square value={squares[8]} onSquareClick={() => handleClick(8)}/>
            </div>
        </>
    );
}

function calculateWinner(squares) {
    // 横3 竖3 俩对角线
    const lines =
        [
            [0, 1, 2],
            [3, 4, 5],
            [6, 7, 8],
            [0, 3, 6],
            [1, 4, 7],
            [2, 5, 8],
            [0, 4, 8],
            [2, 4, 6]
        ];
    for (let i = 0; i < lines.length; i++) {
        const [a, b, c] = lines[i];
        if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
            return squares[a];
        }
    }
    return null;

}

export default function Game() {
    const [history, setHistory] = useState([Array(9).fill(null)]);
    // 记录当前用户正在查看的步骤
    const [currentMove, setCurrentMove] = useState(0);
    const currentSquares = history[currentMove];
    const turnX = currentMove % 2 === 0

    function handlePlay(nextSquares) {
        const nextHistory = [...history.slice(0, currentMove + 1), nextSquares]
        setHistory(nextHistory);
        setCurrentMove(nextHistory.length - 1);
    }

    function jumpTo(nextMove) {
        setCurrentMove(nextMove)
    }

    const moves = history.map((squares, move) => {
        let description;
        if (move > 0) {
            description = 'Go to move #' + move;
        } else {
            description = 'Go to game start';
        }
        return (
            <li key={move}>
                <button onClick={() => jumpTo(move)}>{description}</button>
            </li>
        );
    })
    return (
        <div className='game'>
            <div className="game-board">
                <Board turnX={turnX} squares={currentSquares} onPlay={handlePlay}></Board>
            </div>
            <div className="game-info">
                <ol>{moves}</ol>
            </div>
        </div>
    )
}

