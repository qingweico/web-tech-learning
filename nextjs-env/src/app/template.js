export default function Template({ children }) {
    return (
        <div className="template-container">
            <div className="template-sidebar">
                <p>Sidebar content</p>
            </div>
            <div className="template-content">
                {children}
            </div>
        </div>
    );
}
