import {createContext, useContext, useState} from 'react';

type Theme = "light" | "dark" | "system";
const ThemeContext = createContext<Theme>("system");
const useGetTheme = () => useContext(ThemeContext)


export default function ThemeApp() {
    const [theme, setTheme] = useState<Theme>('light');

    return (
        <ThemeContext.Provider value={theme}>
            <Context></Context>
        </ThemeContext.Provider>
    )
}

function Context() {
    const theme = useGetTheme();

    return (<>
        <div>
            <p>当前主题: {theme}</p>
        </div>
    </>)
}

type ComplexObject = {
    kind: string
};
const context  = createContext<ComplexObject | null>(null);

// 运行时检查 context
const useGetComplexObject = () => {
    const object = useContext(context);
    if (!object) { throw new Error("useGetComplexObject must be used within a Provider") }
    return object;
}
