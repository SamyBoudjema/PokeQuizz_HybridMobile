class UIManager {
    constructor() {
        this.elements = {};
        this.initializeDOMElements();
    }

    initializeDOMElements() {
        const ids = [
            'home-screen', 'quiz-screen', 'leaderboard-screen', 'start-quiz-btn', 'leaderboard-btn',
            'back-to-home', 'back-to-home-leaderboard', 'question-counter', 'progress-fill',
            'current-score', 'timer', 'pokemon-image', 'image-loading', 'pokemon-answer',
            'submit-answer', 'answer-feedback', 'feedback-content', 'next-question',
            'quiz-content', 'quiz-results', 'final-score-text', 'final-time-text',
            'final-percentage', 'congratulations-message', 'player-name', 'save-score',
            'play-again', 'view-leaderboard', 'refresh-leaderboard', 'leaderboard-empty',
            'leaderboard-table', 'scores-list', 'total-games', 'best-score', 'best-time',
            'clear-scores', 'new-quiz', 'start-first-quiz', 'loading-indicator'
        ];
        
        ids.forEach(id => {
            this.elements[this.toCamelCase(id)] = document.getElementById(id);
        });
    }

    toCamelCase(str) {
        return str.replace(/-([a-z])/g, (g) => g[1].toUpperCase());
    }

    showScreen(screenName) {
        document.querySelectorAll('.screen').forEach(screen => {
            screen.classList.remove('active');
        });
        const targetScreen = document.getElementById(`${screenName}-screen`);
        if (targetScreen) targetScreen.classList.add('active');
    }

    showLoading(show) {
        this.elements.loadingIndicator.classList.toggle('hidden', !show);
    }

    showImageLoading(show) {
        this.elements.imageLoading.classList.toggle('hidden', !show);
        this.elements.pokemonImage.style.display = show ? 'none' : 'block';
    }

    resetQuizInterface() {
        this.elements.questionCounter.textContent = 'Question 1/10';
        this.elements.progressFill.style.width = '0%';
        this.elements.currentScore.textContent = 'Score: 0';
        this.elements.timer.textContent = 'Temps: 0:00';
        this.elements.pokemonAnswer.value = '';
        this.elements.playerName.value = '';
        
        [this.elements.pokemonAnswer, this.elements.submitAnswer].forEach(el => {
            el.disabled = false;
        });
        this.elements.saveScore.disabled = false;
        this.elements.saveScore.textContent = 'Sauvegarder';
        
        this.elements.answerFeedback.classList.add('hidden');
        this.elements.quizResults.classList.add('hidden');
        this.elements.quizContent.classList.remove('hidden');
        this.elements.pokemonImage.src = '';
        this.showImageLoading(false);
    }

    updateQuizProgress(data) {
        this.elements.questionCounter.textContent = `Question ${data.questionNumber}/${data.totalQuestions}`;
        this.elements.progressFill.style.width = `${data.progress}%`;
        this.elements.pokemonImage.src = data.pokemon.blackWhiteImage;
        this.elements.pokemonAnswer.focus();
    }

    showAnswerFeedback(result) {
        const content = result.isCorrect ? 
            `<div class="feedback-success"><h3>✅ Correct !</h3><p>C'était bien <strong>${result.correctAnswer}</strong></p></div>` :
            `<div class="feedback-error"><h3>❌ Incorrect</h3><p>Vous avez répondu: <strong>${result.userAnswer}</strong></p><p>La bonne réponse était: <strong>${result.correctAnswer}</strong></p></div>`;
        
        this.elements.feedbackContent.innerHTML = content;
        this.elements.answerFeedback.classList.remove('hidden');
        this.elements.nextQuestion.textContent = result.isQuizComplete ? 'Voir les résultats' : 'Question suivante';
    }

    showQuizResults(results, message) {
        this.elements.quizContent.classList.add('hidden');
        this.elements.finalScoreText.textContent = `Score: ${results.score}/${results.totalQuestions}`;
        this.elements.finalTimeText.textContent = `Temps: ${results.formattedTime}`;
        this.elements.finalPercentage.textContent = `${results.percentage}%`;
        this.elements.congratulationsMessage.textContent = message;
        this.elements.playerName.focus();
        this.elements.quizResults.classList.remove('hidden');
    }

    updateLeaderboardStats(stats) {
        this.elements.totalGames.textContent = stats.totalGames || 0;
        this.elements.bestScore.textContent = stats.bestScore ? `${stats.bestScore}/10` : '0/10';
        this.elements.bestTime.textContent = stats.formattedBestTime || '0:00';
    }

    displayScores(scores) {
        this.elements.scoresList.innerHTML = '';
        if (scores.length === 0) {
            this.elements.leaderboardEmpty.classList.remove('hidden');
            this.elements.leaderboardTable.classList.add('hidden');
        } else {
            this.elements.leaderboardEmpty.classList.add('hidden');
            this.elements.leaderboardTable.classList.remove('hidden');
            scores.forEach(score => {
                const scoreElement = document.createElement('div');
                scoreElement.className = 'score-row';
                scoreElement.innerHTML = `
                    <span class="col-rank">${score.medal}</span>
                    <span class="col-name">${score.name}</span>
                    <span class="col-score">${score.score}/${score.totalQuestions}</span>
                    <span class="col-time">${score.formattedTime}</span>
                    <span class="col-date">${score.formattedDate}</span>
                `;
                this.elements.scoresList.appendChild(scoreElement);
            });
        }
    }
}

window.UIManager = UIManager;