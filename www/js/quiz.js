class QuizManager {
    constructor(pokemonAPI, storageManager) {
        this.pokemonAPI = pokemonAPI;
        this.storageManager = storageManager;
        this.reset();
    }

    reset() {
        this.currentQuestion = 0;
        this.totalQuestions = 10;
        this.score = 0;
        this.startTime = null;
        this.endTime = null;
        this.currentPokemon = null;
        this.isQuizActive = false;
        this.questions = [];
        this.userAnswers = [];
    }

    async startQuiz() {
        this.reset();
        this.startTime = Date.now();
        this.isQuizActive = true;
        await this.loadNextQuestion();
    }

    async loadNextQuestion() {
        if (this.currentQuestion >= this.totalQuestions) {
            return this.endQuiz();
        }

        this.currentPokemon = await this.pokemonAPI.getRandomPokemonWithBlackWhiteImage();
        this.questions.push({
            questionNumber: this.currentQuestion + 1,
            pokemon: this.currentPokemon,
            timestamp: Date.now()
        });

        return {
            questionNumber: this.currentQuestion + 1,
            totalQuestions: this.totalQuestions,
            pokemon: this.currentPokemon,
            progress: ((this.currentQuestion + 1) / this.totalQuestions) * 100
        };
    }

    async submitAnswer(userAnswer) {
        if (!this.isQuizActive || !this.currentPokemon) {
            throw new Error('No active quiz');
        }

        const isCorrect = this.pokemonAPI.checkAnswer(userAnswer, this.currentPokemon.name);
        const answerData = {
            questionNumber: this.currentQuestion + 1,
            userAnswer: userAnswer.trim(),
            correctAnswer: this.currentPokemon.name,
            isCorrect: isCorrect,
            pokemon: this.currentPokemon,
            timestamp: Date.now()
        };

        this.userAnswers.push(answerData);
        if (isCorrect) {
            this.score++;
            // Vibration pour réponse correcte
            this.triggerVibration('correct');
        } else {
            // Vibration pour réponse incorrecte
            this.triggerVibration('wrong');
        }
        this.currentQuestion++;

        return {
            isCorrect,
            correctAnswer: this.currentPokemon.name,
            userAnswer: userAnswer.trim(),
            currentScore: this.score,
            questionNumber: this.currentQuestion,
            totalQuestions: this.totalQuestions,
            isQuizComplete: this.currentQuestion >= this.totalQuestions
        };
    }

    endQuiz() {
        this.endTime = Date.now();
        this.isQuizActive = false;
        const totalTime = Math.round((this.endTime - this.startTime) / 1000);
        
        // Vibration pour fin de jeu
        this.triggerVibration('gameEnd');
        
        return {
            score: this.score,
            totalQuestions: this.totalQuestions,
            percentage: Math.round((this.score / this.totalQuestions) * 100),
            totalTime,
            formattedTime: this.formatTime(totalTime),
            questions: this.questions,
            answers: this.userAnswers,
            startTime: this.startTime,
            endTime: this.endTime
        };
    }

    async saveQuizResults(playerName, quizResults) {
        const scoreData = {
            name: playerName.trim(),
            score: quizResults.score,
            time: quizResults.totalTime,
            percentage: quizResults.percentage,
            date: new Date().toISOString(),
            totalQuestions: quizResults.totalQuestions
        };
        await this.storageManager.saveScore(scoreData);
    }

    getElapsedTime() {
        return this.startTime ? Math.round((Date.now() - this.startTime) / 1000) : 0;
    }

    formatTime(timeInSeconds) {
        const minutes = Math.floor(timeInSeconds / 60);
        const seconds = Math.floor(timeInSeconds % 60);
        return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    }

    triggerVibration(type) {
        if (typeof cordova !== 'undefined' && cordova.plugins && cordova.plugins.AdvancedVibration) {
            const vibration = cordova.plugins.AdvancedVibration;
            
            switch(type) {
                case 'correct':
                    vibration.correctAnswer();
                    break;
                case 'wrong':
                    vibration.wrongAnswer();
                    break;
                case 'gameEnd':
                    vibration.gameEnd();
                    break;
                default:
                    console.log('Type de vibration non reconnu:', type);
            }
        } else {
            console.log('Plugin AdvancedVibration non disponible');
        }
    }
}

window.QuizManager = QuizManager;