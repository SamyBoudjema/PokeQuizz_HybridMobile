class StorageManager {
    constructor() {
        this.fileName = 'pokequizz_scores.json';
        this.fileSystem = null;
        this.isReady = false;
    }

    async initialize() {
        return new Promise((resolve) => {
            document.addEventListener('deviceready', () => {
                window.requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem;
                
                if (window.requestFileSystem) {
                    window.requestFileSystem(window.PERSISTENT, 5 * 1024 * 1024, (fs) => {
                        this.fileSystem = fs;
                        this.isReady = true;
                        resolve();
                    }, () => {
                        this.isReady = true;
                        resolve();
                    });
                } else {
                    this.isReady = true;
                    resolve();
                }
            }, false);
        });
    }

    async saveScore(scoreData) {
        const scores = await this.loadScores();
        const newScore = {
            name: scoreData.name,
            score: scoreData.score,
            time: scoreData.time,
            date: new Date().toISOString(),
            id: Date.now()
        };
        scores.push(newScore);
        scores.sort((a, b) => b.score !== a.score ? b.score - a.score : a.time - b.time);
        await this.writeScores(scores);
    }

    async loadScores() {
        try {
            return this.fileSystem ? await this.readFromFile() : this.readFromLocalStorage();
        } catch (error) {
            return [];
        }
    }

    async getTopScores(limit = 5) {
        const scores = await this.loadScores();
        return scores.slice(0, limit);
    }

    readFromFile() {
        return new Promise((resolve) => {
            this.fileSystem.root.getFile(this.fileName, { create: true }, (fileEntry) => {
                fileEntry.file((file) => {
                    const reader = new FileReader();
                    reader.onloadend = () => {
                        try {
                            resolve(reader.result ? JSON.parse(reader.result) : []);
                        } catch {
                            resolve([]);
                        }
                    };
                    reader.readAsText(file);
                });
            }, () => resolve([]));
        });
    }

    writeScores(scores) {
        if (this.fileSystem) {
            return this.writeToFile(scores);
        } else {
            localStorage.setItem('pokequizz_scores', JSON.stringify(scores));
            return Promise.resolve();
        }
    }

    writeToFile(scores) {
        return new Promise((resolve, reject) => {
            this.fileSystem.root.getFile(this.fileName, { create: true }, (fileEntry) => {
                fileEntry.createWriter((fileWriter) => {
                    fileWriter.onwriteend = resolve;
                    fileWriter.onerror = reject;
                    const blob = new Blob([JSON.stringify(scores, null, 2)], { type: 'application/json' });
                    fileWriter.write(blob);
                });
            }, reject);
        });
    }

    readFromLocalStorage() {
        try {
            const scores = localStorage.getItem('pokequizz_scores');
            return scores ? JSON.parse(scores) : [];
        } catch {
            return [];
        }
    }

    async clearAllScores() {
        await this.writeScores([]);
    }

    formatTime(timeInSeconds) {
        const minutes = Math.floor(timeInSeconds / 60);
        const seconds = Math.floor(timeInSeconds % 60);
        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    }
}

window.StorageManager = StorageManager;