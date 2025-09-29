class LeaderboardManager {
    constructor(storageManager) {
        this.storageManager = storageManager;
    }

    async getTopScores(limit = 5) {
        const scores = await this.storageManager.getTopScores(limit);
        return scores.map((score, index) => ({
            rank: index + 1,
            name: score.name,
            score: score.score,
            totalQuestions: score.totalQuestions || 10,
            percentage: score.percentage || Math.round((score.score / 10) * 100),
            time: score.time,
            formattedTime: this.storageManager.formatTime(score.time),
            date: score.date,
            formattedDate: this.formatDate(score.date),
            medal: this.getMedal(index + 1)
        }));
    }

    async getLeaderboardStats() {
        const allScores = await this.storageManager.loadScores();
        if (allScores.length === 0) {
            return { totalGames: 0, averageScore: 0, bestScore: 0, bestTime: 0, totalPlayers: 0 };
        }

        const totalGames = allScores.length;
        const averageScore = allScores.reduce((sum, score) => sum + score.score, 0) / totalGames;
        const bestScore = Math.max(...allScores.map(s => s.score));
        const bestTime = Math.min(...allScores.map(s => s.time));
        const uniquePlayers = new Set(allScores.map(s => s.name.toLowerCase())).size;

        return {
            totalGames,
            averageScore: Math.round(averageScore * 100) / 100,
            bestScore,
            bestTime,
            formattedBestTime: this.storageManager.formatTime(bestTime),
            totalPlayers: uniquePlayers
        };
    }

    getMedal(rank) {
        const medals = { 1: '🥇', 2: '🥈', 3: '🥉' };
        return medals[rank] || `#${rank}`;
    }

    formatDate(dateString) {
        try {
            const date = new Date(dateString);
            const now = new Date();
            const diffTime = Math.abs(now - date);
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

            if (diffDays === 1) return 'Aujourd\'hui';
            if (diffDays === 2) return 'Hier';
            if (diffDays <= 7) return `Il y a ${diffDays - 1} jours`;
            
            return date.toLocaleDateString('fr-FR', {
                day: '2-digit', month: '2-digit', year: 'numeric'
            });
        } catch {
            return 'Date inconnue';
        }
    }

    getCongratulationMessage(score, totalQuestions, rank) {
        const percentage = (score / totalQuestions) * 100;
        
        if (rank === 1) return '🏆 Nouveau record ! Vous êtes le meilleur dresseur !';
        if (rank <= 3) return `${this.getMedal(rank)} Excellent ! Vous êtes sur le podium !`;
        if (rank <= 5) return '⭐ Très bien ! Vous êtes dans le top 5 !';
        if (percentage >= 80) return '👏 Bravo ! Excellent score !';
        if (percentage >= 60) return '👍 Bien joué ! Bon résultat !';
        if (percentage >= 40) return '💪 Pas mal ! Continuez à vous entraîner !';
        
        return '🎯 Il faut encore s\'entraîner, mais ne baissez pas les bras !';
    }

    async clearLeaderboard() {
        await this.storageManager.clearAllScores();
    }
}

window.LeaderboardManager = LeaderboardManager;