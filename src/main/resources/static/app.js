document.addEventListener('DOMContentLoaded', async () => {
    const searchForm = document.getElementById('searchForm');
    const searchInput = document.getElementById('searchInput');
    const searchType = document.getElementById('searchType');
    const sortAsc = document.getElementById('sortAsc');
    const sortDesc = document.getElementById('sortDesc');
    const pokemonList = document.getElementById('pokemonList');
    const pokemonCard = document.getElementById('pokemonCard');
    const loadingSpinner = document.getElementById('loadingSpinner');
    const errorMessage = document.getElementById('errorMessage');

    let allPokemon = [];
    let currentSort = 'asc';

    async function loadInitialPokemon() {
        try {
            showLoading();
            const response = await fetch('http://localhost:8080/api/pokemon');
            allPokemon = await response.json();
            hideLoading();
            sortAndDisplayPokemon();

            if (allPokemon.length > 0) {
                displayPokemon(allPokemon[0]);
            }
        } catch (error) {
            showError();
        }
    }

    await loadInitialPokemon();

    function sortAndDisplayPokemon(filterText = '') {
        if (!allPokemon.length) return;
        let filteredPokemon = [...allPokemon];

        if (filterText) {
            filterText = filterText.toLowerCase();
            switch (searchType.value) {
                case 'name':
                    filteredPokemon = filteredPokemon.filter(p => p.name.toLowerCase().startsWith(filterText));
                    break;
                case 'ability':
                    filteredPokemon = filteredPokemon.filter(p => p.abilities.some(a => (a.name || a).toLowerCase().includes(filterText)));
                    break;
                case 'type':
                    filteredPokemon = filteredPokemon.filter(p => p.types.some(t => (t.name || t).toLowerCase().includes(filterText)));
                    break;
            }
        }

        filteredPokemon.sort((a, b) => currentSort === 'asc' ? a.name.localeCompare(b.name) : b.name.localeCompare(a.name));

        pokemonList.innerHTML = '';
        filteredPokemon.forEach(pokemon => {
            const listItem = document.createElement('div');
            listItem.className = 'pokemon-list-item';
            listItem.innerHTML = `<span>${pokemon.name}</span>`;
            listItem.addEventListener('click', () => {
                document.querySelectorAll('.pokemon-list-item').forEach(item => item.classList.remove('active'));
                listItem.classList.add('active');
                displayPokemon(pokemon);
            });
            pokemonList.appendChild(listItem);
        });
    }

    function displayPokemon(pokemon) {
        errorMessage.classList.add('hidden');
        pokemonCard.classList.remove('hidden');
        loadingSpinner.classList.add('hidden');

        document.getElementById('pokemonName').textContent = pokemon.name;
        document.getElementById('pokemonId').textContent = `#${String(pokemon.id).padStart(3, '0')}`;
        document.getElementById('pokemonHeight').textContent = `${pokemon.height / 10}m`;
        document.getElementById('pokemonWeight').textContent = `${pokemon.weight / 10}kg`;

        // 🟢 Mostrar correctamente la imagen del Pokémon
        const pokemonImage = document.getElementById('pokemonImage');
        if (pokemon.imageUrl) {
            pokemonImage.src = pokemon.imageUrl;
        } else if (pokemon.image) {
            pokemonImage.src = pokemon.image;
        } else {
            pokemonImage.src = 'default-image.png'; // Imagen por defecto si no hay imagen disponible
        }
        pokemonImage.alt = pokemon.name;


        const typesContainer = document.getElementById('pokemonTypes');
        typesContainer.innerHTML = '';
        pokemon.types.forEach(typeObj => {
            const typeName = typeObj.name || typeObj;
            const typeElement = document.createElement('span');
            typeElement.className = 'type-badge';
            typeElement.textContent = typeName;
            typeElement.style.backgroundColor = getTypeColor(typeName);
            typesContainer.appendChild(typeElement);
        });


        const abilitiesList = document.getElementById('abilitiesList');
        abilitiesList.innerHTML = '';
        pokemon.abilities.forEach(abilityObj => {
            const abilityName = abilityObj.name || abilityObj;
            const abilityElement = document.createElement('span');
            abilityElement.className = 'ability-badge';
            abilityElement.textContent = abilityName;
            abilitiesList.appendChild(abilityElement);
        });
    }


    function showLoading() {
        loadingSpinner.classList.remove('hidden');
        pokemonCard.classList.add('hidden');
        errorMessage.classList.add('hidden');
    }

    function hideLoading() {
        loadingSpinner.classList.add('hidden');
    }

    function showError() {
        errorMessage.classList.remove('hidden');
        pokemonCard.classList.add('hidden');
        loadingSpinner.classList.add('hidden');
    }

    function getTypeColor(type) {
        const colors = {
            normal: '#A8A878', fire: '#F08030', water: '#6890F0', electric: '#F8D030', grass: '#78C850',
            ice: '#98D8D8', fighting: '#C03028', poison: '#A040A0', ground: '#E0C068', flying: '#A890F0',
            psychic: '#F85888', bug: '#A8B820', rock: '#B8A038', ghost: '#705898', dragon: '#7038F8',
            dark: '#705848', steel: '#B8B8D0', fairy: '#EE99AC'
        };
        return colors[type] || '#777777';
    }

    searchForm.addEventListener('submit', (e) => {
        e.preventDefault();
        sortAndDisplayPokemon(searchInput.value.trim());
    });

    searchInput.addEventListener('input', (e) => {
        sortAndDisplayPokemon(e.target.value.trim());
    });

    searchType.addEventListener('change', () => {
        searchInput.value = '';
        sortAndDisplayPokemon();
    });

    sortAsc.addEventListener('click', () => {
        currentSort = 'asc';
        sortAndDisplayPokemon(searchInput.value.trim());
    });

    sortDesc.addEventListener('click', () => {
        currentSort = 'desc';
        sortAndDisplayPokemon(searchInput.value.trim());
    });

});
