import { useState } from 'react'

function App() {
    const [url, setUrl] = useState('')
    const [shortUrl, setShortUrl] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)

    const handleSubmit = async (e) => {
        e.preventDefault()
        setError('')
        setShortUrl('')
        setLoading(true)

        try {
            const response = await fetch('http://localhost:8080/api/v1/shorten', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ url }),
            })

            if (!response.ok) {
                throw new Error('Failed to shorten URL')
            }

            const data = await response.json()
            setShortUrl(data.shortUrl)
        } catch (err) {
            setError(err.message || 'Something went wrong')
        } finally {
            setLoading(false)
        }
    }

    return (
        <>
            <div className="card">
                <h1>URL Shortener</h1>
                <p>Enter a long URL to get a shortened version.</p>

                <form onSubmit={handleSubmit}>
                    <input
                        type="url"
                        placeholder="https://example.com/very/long/url..."
                        value={url}
                        onChange={(e) => setUrl(e.target.value)}
                        required
                    />
                    <button type="submit" disabled={loading}>
                        {loading ? 'Shortening...' : 'Shorten URL'}
                    </button>
                </form>

                {error && (
                    <div className="error">
                        <strong>Error:</strong> {error}
                    </div>
                )}

                {shortUrl && (
                    <div className="result">
                        <p>Shortened URL:</p>
                        <a href={shortUrl} target="_blank" rel="noopener noreferrer" className="link">
                            {shortUrl}
                        </a>
                    </div>
                )}
            </div>
        </>
    )
}

export default App
