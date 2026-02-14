import { useEffect, useState } from 'react';

function App() {
    const [message, setMessage] = useState("");

    useEffect(() => {
        fetch("http://localhost:8080/api/hello")
            .then(response => response.text())
            .then(data => setMessage(data))
            .catch(err => console.log(err));
    }, []);

    return (
        <div>
            <h1>Music Shop</h1>
            <p>Zpráva z backendu: {message}</p>
        </div>
    );
}

export default App;