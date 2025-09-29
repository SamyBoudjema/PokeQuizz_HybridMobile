/**
 * Application PokéQuizz - Point d'entrée principal
 * Orchestre tous les modules et gère la navigation
 */

class PokeQuizzApp {
    constructor() {
        // Instances des modules
        this.pokemonAPI = null;
        this.storageManager = null;
        this.quizManager = null;
        this.leaderboardManager = null;
        
        // État de l'application
        this.currentScreen = 'home';
        this.isInitialized = false;
        this.quizTimer = null;
        
        // Éléments DOM
        this.elements = {};
        
        // Bind des méthodes
        this.initializeApp = this.initializeApp.bind(this);
        this.setupEventListeners = this.setupEventListeners.bind(this);
        this.showScreen = this.showScreen.bind(this);
    }

    /**
     * Initialise l'application après que Cordova soit prêt
     */
    async initializeApp() {
        try {
            console.log('Initialisation de PokéQuizz...');
            
            // Initialiser les éléments DOM
            this.initializeDOMElements();
            
            // Initialiser les modules
            this.pokemonAPI = new PokemonAPI();
            this.storageManager = new StorageManager();
            this.quizManager = new QuizManager(this.pokemonAPI, this.storageManager);
            this.leaderboardManager = new LeaderboardManager(this.storageManager);
            
            // Initialiser le stockage
            await this.storageManager.initialize();
            
            // Configurer les événements
            this.setupEventListeners();
            
            // Charger les données initiales
            await this.loadInitialData();
            
            this.isInitialized = true;
            console.log('PokéQuizz initialisé avec succès !');
            
        } catch (error) {
            console.error('Erreur lors de l\'initialisation:', error);
            this.showError('Erreur d\'initialisation de l\'application');
        }
    }

    /**
     * Initialise les références aux éléments DOM
     */
    initializeDOMElements() {
        // Écrans
        this.elements.homeScreen = document.getElementById('home-screen');
        this.elements.quizScreen = document.getElementById('quiz-screen');
        this.elements.leaderboardScreen = document.getElementById('leaderboard-screen');
        
        // Boutons navigation principale
        this.elements.startQuizBtn = document.getElementById('start-quiz-btn');
        this.elements.leaderboardBtn = document.getElementById('leaderboard-btn');
        this.elements.backToHome = document.getElementById('back-to-home');
        this.elements.backToHomeLeaderboard = document.getElementById('back-to-home-leaderboard');
        
        // Quiz elements
        this.elements.questionCounter = document.getElementById('question-counter');
        this.elements.progressFill = document.getElementById('progress-fill');
        this.elements.currentScore = document.getElementById('current-score');
        this.elements.timer = document.getElementById('timer');
        this.elements.pokemonImage = document.getElementById('pokemon-image');
        this.elements.imageLoading = document.getElementById('image-loading');
        this.elements.pokemonAnswer = document.getElementById('pokemon-answer');
        this.elements.submitAnswer = document.getElementById('submit-answer');
        this.elements.answerFeedback = document.getElementById('answer-feedback');
        this.elements.feedbackContent = document.getElementById('feedback-content');
        this.elements.nextQuestion = document.getElementById('next-question');
        this.elements.quizContent = document.getElementById('quiz-content');
        this.elements.quizResults = document.getElementById('quiz-results');
        
        // Résultats du quiz
        this.elements.finalScoreText = document.getElementById('final-score-text');
        this.elements.finalTimeText = document.getElementById('final-time-text');
        this.elements.finalPercentage = document.getElementById('final-percentage');
        this.elements.congratulationsMessage = document.getElementById('congratulations-message');
        this.elements.playerName = document.getElementById('player-name');
        this.elements.saveScore = document.getElementById('save-score');
        this.elements.playAgain = document.getElementById('play-again');
        this.elements.viewLeaderboard = document.getElementById('view-leaderboard');
        
        // Leaderboard elements
        this.elements.refreshLeaderboard = document.getElementById('refresh-leaderboard');
        this.elements.leaderboardEmpty = document.getElementById('leaderboard-empty');
        this.elements.leaderboardTable = document.getElementById('leaderboard-table');
        this.elements.scoresList = document.getElementById('scores-list');
        this.elements.totalGames = document.getElementById('total-games');
        this.elements.bestScore = document.getElementById('best-score');
        this.elements.bestTime = document.getElementById('best-time');
        this.elements.clearScores = document.getElementById('clear-scores');
        this.elements.newQuiz = document.getElementById('new-quiz');
        this.elements.startFirstQuiz = document.getElementById('start-first-quiz');
        
        // Loading
        this.elements.loadingIndicator = document.getElementById('loading-indicator');
    }

    /**
     * Configure tous les événements de l'interface
     */
    setupEventListeners() {
        // Navigation principale
        this.elements.startQuizBtn.addEventListener('click', () => this.startNewQuiz());
        this.elements.leaderboardBtn.addEventListener('click', () => this.showLeaderboard());
        this.elements.backToHome.addEventListener('click', () => this.showScreen('home'));
        this.elements.backToHomeLeaderboard.addEventListener('click', () => this.showScreen('home'));
        
        // Quiz interactions
        this.elements.submitAnswer.addEventListener('click', () => this.submitAnswer());
        this.elements.pokemonAnswer.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.submitAnswer();
        });
        this.elements.nextQuestion.addEventListener('click', () => this.loadNextQuestion());
        
        // Résultats du quiz
        this.elements.saveScore.addEventListener('click', () => this.saveQuizScore());
        this.elements.playAgain.addEventListener('click', () => this.startNewQuiz());
        this.elements.viewLeaderboard.addEventListener('click', () => this.showLeaderboard());
        
        // Leaderboard actions
        this.elements.refreshLeaderboard.addEventListener('click', () => this.loadLeaderboard());
        this.elements.clearScores.addEventListener('click', () => this.clearAllScores());
        this.elements.newQuiz.addEventListener('click', () => this.startNewQuiz());
        this.elements.startFirstQuiz.addEventListener('click', () => this.startNewQuiz());
        
        // Validation du nom joueur
        this.elements.playerName.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.saveQuizScore();
        });
    }

    /**
     * Charge les données initiales de l'application
     */
    async loadInitialData() {
        try {
            // Charger les statistiques du leaderboard pour l'écran d'accueil
            await this.loadLeaderboard();
        } catch (error) {
            console.error('Erreur chargement données initiales:', error);
        }
    }

    /**
     * Affiche un écran spécifique
     */
    showScreen(screenName) {
        // Masquer tous les écrans
        document.querySelectorAll('.screen').forEach(screen => {
            screen.classList.remove('active');
        });
        
        // Afficher l'écran demandé
        const targetScreen = document.getElementById(`${screenName}-screen`);
        if (targetScreen) {
            targetScreen.classList.add('active');
            this.currentScreen = screenName;
        }
    }

    /**
     * Démarre un nouveau quiz
     */
    async startNewQuiz() {
        try {
            this.showLoading(true);
            this.showScreen('quiz');
            
            // Réinitialiser l'interface du quiz
            this.resetQuizInterface();
            
            // Démarrer le quiz
            await this.quizManager.startQuiz();
            
            // Charger la première question
            await this.loadNextQuestion();
            
            // Démarrer le timer
            this.startTimer();
            
            this.showLoading(false);
            
        } catch (error) {
            console.error('Erreur démarrage quiz:', error);
            this.showError('Erreur lors du démarrage du quiz');
            this.showLoading(false);
        }
    }

    /**
     * Charge la question suivante du quiz
     */
    async loadNextQuestion() {
        try {
            this.showImageLoading(true);
            
            // Masquer le feedback et réinitialiser l'input
            this.elements.answerFeedback.classList.add('hidden');
            this.elements.pokemonAnswer.value = '';
            this.elements.pokemonAnswer.disabled = false;
            this.elements.submitAnswer.disabled = false;
            
            // Charger la question depuis le quiz manager
            const questionData = await this.quizManager.loadNextQuestion();
            
            // Si le quiz est terminé
            if (!questionData || questionData.questionNumber > this.quizManager.totalQuestions) {
                this.showQuizResults();
                return;
            }
            
            // Mettre à jour l'interface
            this.elements.questionCounter.textContent = `Question ${questionData.questionNumber}/${questionData.totalQuestions}`;
            this.elements.progressFill.style.width = `${questionData.progress}%`;
            
            // Charger l'image du Pokémon
            this.elements.pokemonImage.src = questionData.pokemon.blackWhiteImage;
            this.elements.pokemonImage.alt = 'Pokémon mystère';
            
            // Focus sur l'input
            this.elements.pokemonAnswer.focus();
            
            this.showImageLoading(false);
            
        } catch (error) {
            console.error('Erreur chargement question:', error);
            this.showError('Erreur lors du chargement de la question');
            this.showImageLoading(false);
        }
    }

    /**
     * Soumet la réponse de l'utilisateur
     */
    async submitAnswer() {
        const answer = this.elements.pokemonAnswer.value.trim();
        
        if (!answer) {
            this.showError('Veuillez entrer une réponse');
            return;
        }

        try {
            // Désactiver l'input et le bouton
            this.elements.pokemonAnswer.disabled = true;
            this.elements.submitAnswer.disabled = true;
            
            // Soumettre la réponse
            const result = await this.quizManager.submitAnswer(answer);
            
            // Afficher le feedback
            this.showAnswerFeedback(result);
            
            // Mettre à jour le score affiché
            this.elements.currentScore.textContent = `Score: ${result.currentScore}`;
            
        } catch (error) {
            console.error('Erreur soumission réponse:', error);
            this.showError('Erreur lors de la soumission de la réponse');
            
            // Réactiver l'interface en cas d'erreur
            this.elements.pokemonAnswer.disabled = false;
            this.elements.submitAnswer.disabled = false;
        }
    }

    /**
     * Affiche le feedback pour une réponse
     */
    showAnswerFeedback(result) {
        const feedbackContent = this.elements.feedbackContent;
        
        if (result.isCorrect) {
            feedbackContent.innerHTML = `
                <div class="feedback-success">
                    <h3>✅ Correct !</h3>
                    <p>C'était bien <strong>${result.correctAnswer}</strong></p>
                </div>
            `;
        } else {
            feedbackContent.innerHTML = `
                <div class="feedback-error">
                    <h3>❌ Incorrect</h3>
                    <p>Vous avez répondu: <strong>${result.userAnswer}</strong></p>
                    <p>La bonne réponse était: <strong>${result.correctAnswer}</strong></p>
                </div>
            `;
        }
        
        this.elements.answerFeedback.classList.remove('hidden');
        
        // Changer le texte du bouton si c'est la dernière question
        if (result.isQuizComplete) {
            this.elements.nextQuestion.textContent = 'Voir les résultats';
        } else {
            this.elements.nextQuestion.textContent = 'Question suivante';
        }
    }

    /**
     * Affiche les résultats finaux du quiz
     */
    showQuizResults() {
        const results = this.quizManager.endQuiz();
        this.stopTimer();
        
        // Masquer le contenu du quiz
        this.elements.quizContent.classList.add('hidden');
        
        // Afficher les résultats
        this.elements.finalScoreText.textContent = `Score: ${results.score}/${results.totalQuestions}`;
        this.elements.finalTimeText.textContent = `Temps: ${results.formattedTime}`;
        this.elements.finalPercentage.textContent = `${results.percentage}%`;
        
        // Message de félicitations
        const message = this.leaderboardManager.getCongratulationMessage(results.score, results.totalQuestions, 1);
        this.elements.congratulationsMessage.textContent = message;
        
        // Focus sur l'input du nom
        this.elements.playerName.focus();
        
        this.elements.quizResults.classList.remove('hidden');
    }

    /**
     * Sauvegarde le score du quiz
     */
    async saveQuizScore() {
        const playerName = this.elements.playerName.value.trim();
        
        if (!playerName) {
            this.showError('Veuillez entrer votre nom');
            return;
        }

        try {
            const results = this.quizManager.endQuiz();
            await this.quizManager.saveQuizResults(playerName, results);
            
            // Désactiver le bouton après sauvegarde
            this.elements.saveScore.disabled = true;
            this.elements.saveScore.textContent = 'Score sauvegardé !';
            
            // Recharger le leaderboard
            await this.loadLeaderboard();
            
            this.showSuccess('Score sauvegardé avec succès !');
            
        } catch (error) {
            console.error('Erreur sauvegarde score:', error);
            this.showError('Erreur lors de la sauvegarde');
        }
    }

    /**
     * Affiche l'écran de classement
     */
    async showLeaderboard() {
        this.showScreen('leaderboard');
        await this.loadLeaderboard();
    }

    /**
     * Charge les données du classement
     */
    async loadLeaderboard() {
        try {
            const [topScores, stats] = await Promise.all([
                this.leaderboardManager.getTopScores(5),
                this.leaderboardManager.getLeaderboardStats()
            ]);
            
            // Mettre à jour les statistiques
            this.elements.totalGames.textContent = stats.totalGames || 0;
            this.elements.bestScore.textContent = stats.bestScore ? `${stats.bestScore}/10` : '0/10';
            this.elements.bestTime.textContent = stats.formattedBestTime || '0:00';
            
            // Afficher les scores
            if (topScores.length === 0) {
                this.elements.leaderboardEmpty.classList.remove('hidden');
                this.elements.leaderboardTable.classList.add('hidden');
            } else {
                this.elements.leaderboardEmpty.classList.add('hidden');
                this.elements.leaderboardTable.classList.remove('hidden');
                this.displayScores(topScores);
            }
            
        } catch (error) {
            console.error('Erreur chargement leaderboard:', error);
            this.showError('Erreur lors du chargement du classement');
        }
    }

    /**
     * Affiche la liste des scores
     */
    displayScores(scores) {
        const scoresList = this.elements.scoresList;
        scoresList.innerHTML = '';
        
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
            scoresList.appendChild(scoreElement);
        });
    }

    /**
     * Efface tous les scores
     */
    async clearAllScores() {
        if (confirm('Êtes-vous sûr de vouloir effacer tous les scores ?')) {
            try {
                await this.leaderboardManager.clearLeaderboard();
                await this.loadLeaderboard();
                this.showSuccess('Tous les scores ont été effacés');
            } catch (error) {
                console.error('Erreur effacement scores:', error);
                this.showError('Erreur lors de l\'effacement des scores');
            }
        }
    }

    /**
     * Démarre le timer du quiz
     */
    startTimer() {
        this.stopTimer(); // S'assurer qu'aucun timer n'est déjà en cours
        
        this.quizTimer = setInterval(() => {
            const elapsed = this.quizManager.getElapsedTime();
            this.elements.timer.textContent = `Temps: ${this.formatTime(elapsed)}`;
        }, 1000);
    }

    /**
     * Arrête le timer du quiz
     */
    stopTimer() {
        if (this.quizTimer) {
            clearInterval(this.quizTimer);
            this.quizTimer = null;
        }
    }

    /**
     * Remet à zéro l'interface du quiz
     */
    resetQuizInterface() {
        // Réinitialiser les éléments
        this.elements.questionCounter.textContent = 'Question 1/10';
        this.elements.progressFill.style.width = '0%';
        this.elements.currentScore.textContent = 'Score: 0';
        this.elements.timer.textContent = 'Temps: 0:00';
        this.elements.pokemonAnswer.value = '';
        this.elements.playerName.value = '';
        
        // Réinitialiser l'état des boutons
        this.elements.pokemonAnswer.disabled = false;
        this.elements.submitAnswer.disabled = false;
        this.elements.saveScore.disabled = false;
        this.elements.saveScore.textContent = 'Sauvegarder';
        
        // Masquer les éléments
        this.elements.answerFeedback.classList.add('hidden');
        this.elements.quizResults.classList.add('hidden');
        this.elements.quizContent.classList.remove('hidden');
        
        // Réinitialiser l'image
        this.elements.pokemonImage.src = '';
        this.showImageLoading(false);
    }

    /**
     * Affiche/masque l'indicateur de chargement
     */
    showLoading(show) {
        if (show) {
            this.elements.loadingIndicator.classList.remove('hidden');
        } else {
            this.elements.loadingIndicator.classList.add('hidden');
        }
    }

    /**
     * Affiche/masque le chargement d'image
     */
    showImageLoading(show) {
        if (show) {
            this.elements.imageLoading.classList.remove('hidden');
            this.elements.pokemonImage.style.display = 'none';
        } else {
            this.elements.imageLoading.classList.add('hidden');
            this.elements.pokemonImage.style.display = 'block';
        }
    }

    /**
     * Affiche un message d'erreur
     */
    showError(message) {
        // Utiliser une notification simple pour le moment
        alert(`❌ Erreur: ${message}`);
    }

    /**
     * Affiche un message de succès
     */
    showSuccess(message) {
        // Utiliser une notification simple pour le moment
        alert(`✅ ${message}`);
    }

    /**
     * Formate le temps en minutes:secondes
     */
    formatTime(timeInSeconds) {
        const minutes = Math.floor(timeInSeconds / 60);
        const seconds = Math.floor(timeInSeconds % 60);
        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    }
}

// Initialiser l'application quand Cordova est prêt
const app = new PokeQuizzApp();

document.addEventListener('deviceready', app.initializeApp, false);
