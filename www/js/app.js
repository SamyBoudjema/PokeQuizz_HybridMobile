class PokeQuizzApp {
    constructor() {
        this.uiManager = new UIManager();
        this.pokemonAPI = null;
        this.storageManager = null;
        this.quizManager = null;
        this.leaderboardManager = null;
        this.currentScreen = 'home';
        this.isInitialized = false;
        this.quizTimer = null;
    }

    async initializeApp() {
        try {
            this.pokemonAPI = new PokemonAPI();
            this.storageManager = new StorageManager();
            this.quizManager = new QuizManager(this.pokemonAPI, this.storageManager);
            this.leaderboardManager = new LeaderboardManager(this.storageManager);
            
            await this.storageManager.initialize();
            this.setupEventListeners();
            await this.loadInitialData();
            this.isInitialized = true;
        } catch (error) {
            this.showError('Erreur d\'initialisation de l\'application');
        }
    }

    setupEventListeners() {
        const { elements } = this.uiManager;
        
        elements.startQuizBtn.addEventListener('click', () => this.startNewQuiz());
        elements.leaderboardBtn.addEventListener('click', () => this.showLeaderboard());
        elements.backToHome.addEventListener('click', () => this.showScreen('home'));
        elements.backToHomeLeaderboard.addEventListener('click', () => this.showScreen('home'));
        
        elements.submitAnswer.addEventListener('click', () => this.submitAnswer());
        elements.pokemonAnswer.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.submitAnswer();
        });
        elements.nextQuestion.addEventListener('click', () => this.handleNextQuestion());
        
        elements.saveScore.addEventListener('click', () => this.saveQuizScore());
        elements.playAgain.addEventListener('click', () => this.startNewQuiz());
        elements.viewLeaderboard.addEventListener('click', () => this.showLeaderboard());
        
        elements.refreshLeaderboard.addEventListener('click', () => this.loadLeaderboard());
        elements.clearScores.addEventListener('click', () => this.clearAllScores());
        elements.newQuiz.addEventListener('click', () => this.startNewQuiz());
        elements.startFirstQuiz.addEventListener('click', () => this.startNewQuiz());
        
        elements.playerName.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.saveQuizScore();
        });
    }

    async loadInitialData() {
        await this.loadLeaderboard();
    }

    showScreen(screenName) {
        this.uiManager.showScreen(screenName);
        this.currentScreen = screenName;
    }

    async startNewQuiz() {
        this.uiManager.showLoading(true);
        this.showScreen('quiz');
        this.uiManager.resetQuizInterface();
        
        await this.quizManager.startQuiz();
        await this.loadNextQuestion();
        this.startTimer();
        this.uiManager.showLoading(false);
    }

    handleNextQuestion() {
        if (this.quizManager.currentQuestion >= this.quizManager.totalQuestions) {
            this.showQuizResults();
        } else {
            this.loadNextQuestion();
        }
    }

    async loadNextQuestion() {
        if (this.quizManager.currentQuestion >= this.quizManager.totalQuestions) {
            this.showQuizResults();
            return;
        }

        this.uiManager.showImageLoading(true);
        this.uiManager.elements.answerFeedback.classList.add('hidden');
        this.uiManager.elements.pokemonAnswer.value = '';
        [this.uiManager.elements.pokemonAnswer, this.uiManager.elements.submitAnswer]
            .forEach(el => el.disabled = false);
        
        const questionData = await this.quizManager.loadNextQuestion();
        if (!questionData || questionData.questionNumber > this.quizManager.totalQuestions) {
            this.showQuizResults();
            return;
        }

        this.uiManager.updateQuizProgress(questionData);
        this.uiManager.showImageLoading(false);
    }

    async submitAnswer() {
        const answer = this.uiManager.elements.pokemonAnswer.value.trim();
        if (!answer) {
            this.showError('Veuillez entrer une réponse');
            return;
        }

        [this.uiManager.elements.pokemonAnswer, this.uiManager.elements.submitAnswer]
            .forEach(el => el.disabled = true);
        
        const result = await this.quizManager.submitAnswer(answer);
        this.uiManager.showAnswerFeedback(result);
        this.uiManager.elements.currentScore.textContent = `Score: ${result.currentScore}`;
    }

    showQuizResults() {
        const results = this.quizManager.endQuiz();
        this.stopTimer();
        const message = this.leaderboardManager.getCongratulationMessage(results.score, results.totalQuestions, 1);
        this.uiManager.showQuizResults(results, message);
    }

    async saveQuizScore() {
        const playerName = this.uiManager.elements.playerName.value.trim();
        if (!playerName) {
            this.showError('Veuillez entrer votre nom');
            return;
        }

        const results = this.quizManager.endQuiz();
        await this.quizManager.saveQuizResults(playerName, results);
        
        this.uiManager.elements.saveScore.disabled = true;
        this.uiManager.elements.saveScore.textContent = 'Score sauvegardé !';
        await this.loadLeaderboard();
        this.showSuccess('Score sauvegardé avec succès !');
    }

    async showLeaderboard() {
        this.showScreen('leaderboard');
        await this.loadLeaderboard();
    }

    async loadLeaderboard() {
        const [topScores, stats] = await Promise.all([
            this.leaderboardManager.getTopScores(5),
            this.leaderboardManager.getLeaderboardStats()
        ]);
        
        this.uiManager.updateLeaderboardStats(stats);
        this.uiManager.displayScores(topScores);
    }

    async clearAllScores() {
        if (confirm('Êtes-vous sûr de vouloir effacer tous les scores ?')) {
            await this.leaderboardManager.clearLeaderboard();
            await this.loadLeaderboard();
            this.showSuccess('Tous les scores ont été effacés');
        }
    }

    startTimer() {
        this.stopTimer();
        this.quizTimer = setInterval(() => {
            const elapsed = this.quizManager.getElapsedTime();
            this.uiManager.elements.timer.textContent = `Temps: ${this.formatTime(elapsed)}`;
        }, 1000);
    }

    stopTimer() {
        if (this.quizTimer) {
            clearInterval(this.quizTimer);
            this.quizTimer = null;
        }
    }

    showError(message) {
        alert(`❌ Erreur: ${message}`);
    }

    showSuccess(message) {
        alert(`✅ ${message}`);
    }

    formatTime(timeInSeconds) {
        const minutes = Math.floor(timeInSeconds / 60);
        const seconds = Math.floor(timeInSeconds % 60);
        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    }
}

const app = new PokeQuizzApp();
document.addEventListener('deviceready', app.initializeApp.bind(app), false);
