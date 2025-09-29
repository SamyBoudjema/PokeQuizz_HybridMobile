class PokemonAPI {
    constructor() {
        this.baseURL = 'https://tyradex.vercel.app/api/v1/pokemon';
        this.maxPokemonId = 1025;
    }

    generateRandomPokemonId() {
        return Math.floor(Math.random() * this.maxPokemonId) + 1;
    }

    async fetchPokemonById(id) {
        const response = await fetch(`${this.baseURL}/${id}`);
        if (!response.ok) throw new Error(`HTTP Error: ${response.status}`);
        return await response.json();
    }

    async getRandomPokemon() {
        const randomId = this.generateRandomPokemonId();
        const pokemon = await this.fetchPokemonById(randomId);
        return {
            id: pokemon.pokedex_id,
            name: pokemon.name.fr,
            image: pokemon.sprites.regular,
            types: pokemon.types
        };
    }

    async convertToBlackAndWhite(imageUrl) {
        return new Promise((resolve, reject) => {
            const img = new Image();
            img.crossOrigin = 'anonymous';
            
            img.onload = () => {
                const canvas = document.createElement('canvas');
                const ctx = canvas.getContext('2d');
                canvas.width = img.width;
                canvas.height = img.height;
                ctx.drawImage(img, 0, 0);
                
                const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
                const data = imageData.data;
                
                for (let i = 0; i < data.length; i += 4) {
                    const gray = Math.round(0.299 * data[i] + 0.587 * data[i + 1] + 0.114 * data[i + 2]);
                    data[i] = data[i + 1] = data[i + 2] = gray;
                }
                
                ctx.putImageData(imageData, 0, 0);
                resolve(canvas.toDataURL());
            };
            
            img.onerror = () => reject(new Error('Image load failed'));
            img.src = imageUrl;
        });
    }

    async getRandomPokemonWithBlackWhiteImage() {
        const pokemon = await this.getRandomPokemon();
        const blackWhiteImage = await this.convertToBlackAndWhite(pokemon.image);
        return { ...pokemon, originalImage: pokemon.image, blackWhiteImage };
    }

    normalizeName(name) {
        return name.toLowerCase()
                  .normalize('NFD')
                  .replace(/[\u0300-\u036f]/g, '')
                  .replace(/[^a-z0-9]/g, '');
    }

    checkAnswer(userAnswer, correctName) {
        return this.normalizeName(userAnswer) === this.normalizeName(correctName);
    }
}

window.PokemonAPI = PokemonAPI;