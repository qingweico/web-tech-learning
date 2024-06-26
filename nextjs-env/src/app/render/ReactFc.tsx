import React from "react";

interface PowerProps {
    speed: number;
    strength: number;
}

const PlayerPower: React.FC<PowerProps> = ({speed, strength}) => {

    return (
        <>
            <h3>Player Power</h3>
            <p>{speed}</p>
            <p>{strength}</p>
        </>
    )
}

export default PlayerPower
