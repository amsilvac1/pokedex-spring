document.addEventListener('DOMContentLoaded', () => {
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

    // Función para cargar la lista inicial de Pokémon
    async function loadInitialPokemon() {
        try {
            showLoading();
            const response = await fetch('https://pokeapi.co/api/v2/pokemon?limit=151');
            const data = await response.json();
            allPokemon = await Promise.all(
                data.results.map(async (pokemon) => {
                    const detailResponse = await fetch(pokemon.url);
                    return detailResponse.json();
                })
            );
            sortAndDisplayPokemon();
            hideLoading();
            // Cargar el primer Pokémon por defecto
            if (allPokemon.length > 0) {
                displayPokemon(allPokemon[0]);
            }
        } catch (error) {
            showError();
        }
    }

    // Función para ordenar y mostrar la lista de Pokémon
    function sortAndDisplayPokemon(filterText = '') {
        let filteredPokemon = [...allPokemon];

        // Aplicar filtro según el tipo de búsqueda
        if (filterText) {
            filterText = filterText.toLowerCase();
            switch (searchType.value) {
                case 'name':
                    filteredPokemon = filteredPokemon.filter(p =>
                        p.name.toLowerCase().includes(filterText)
                    );
                    break;
                case 'ability':
                    filteredPokemon = filteredPokemon.filter(p =>
                        p.abilities.some(a =>
                            a.ability.name.toLowerCase().includes(filterText)
                        )
                    );
                    break;
                case 'type':
                    filteredPokemon = filteredPokemon.filter(p =>
                        p.types.some(t =>
                            t.type.name.toLowerCase().includes(filterText)
                        )
                    );
                    break;
            }
        }

        // Ordenar la lista
        filteredPokemon.sort((a, b) => {
            if (currentSort === 'asc') {
                return a.name.localeCompare(b.name);
            } else {
                return b.name.localeCompare(a.name);
            }
        });

        // Mostrar la lista en el DOM
        pokemonList.innerHTML = '';
        filteredPokemon.forEach(pokemon => {
            const listItem = document.createElement('div');
            listItem.className = 'pokemon-list-item';
            listItem.innerHTML = `
                <img src="${pokemon.sprites.front_default}" alt="${pokemon.name}">
                <span>${pokemon.name}</span>
            `;
            listItem.addEventListener('click', () => {
                document.querySelectorAll('.pokemon-list-item').forEach(item => {
                    item.classList.remove('active');
                });
                listItem.classList.add('active');
                displayPokemon(pokemon);
            });
            pokemonList.appendChild(listItem);
        });
    }

    // Función para buscar un Pokémon específico
    async function searchPokemon(name) {
        try {
            showLoading();
            const response = await fetch(`https://pokeapi.co/api/v2/pokemon/${name.toLowerCase()}`);

            if (!response.ok) {
                throw new Error('Pokemon not found');
            }

            const data = await response.json();
            displayPokemon(data);
        } catch (error) {
            showError();
        }
    }

    // Función para mostrar el Pokémon
    function displayPokemon(pokemon) {
        errorMessage.classList.add('hidden');
        pokemonCard.classList.remove('hidden');
        loadingSpinner.classList.add('hidden');

        document.getElementById('pokemonName').textContent = pokemon.name;
        document.getElementById('pokemonId').textContent = `#${String(pokemon.id).padStart(3, '0')}`;

        const typesContainer = document.getElementById('pokemonTypes');
        typesContainer.innerHTML = '';
        pokemon.types.forEach(type => {
            const typeElement = document.createElement('span');
            typeElement.className = 'type-badge';
            typeElement.textContent = type.type.name;
            typeElement.style.backgroundColor = getTypeColor(type.type.name);
            typesContainer.appendChild(typeElement);
        });

        const pokemonImage = document.getElementById('pokemonImage');
        pokemonImage.src = pokemon.sprites.other['official-artwork'].front_default;
        pokemonImage.alt = pokemon.name;

        document.getElementById('pokemonHeight').textContent = `${pokemon.height / 10}m`;
        document.getElementById('pokemonWeight').textContent = `${pokemon.weight / 10}kg`;

        const abilitiesList = document.getElementById('abilitiesList');
        abilitiesList.innerHTML = '';
        pokemon.abilities.forEach(ability => {
            const abilityElement = document.createElement('span');
            abilityElement.className = 'ability-badge';
            abilityElement.textContent = ability.ability.name;
            abilitiesList.appendChild(abilityElement);
        });
    }

    // Función para mostrar el loading
    function showLoading() {
        loadingSpinner.classList.remove('hidden');
        pokemonCard.classList.add('hidden');
        errorMessage.classList.add('hidden');
    }

    // Función para mostrar error
    function showError() {
        errorMessage.classList.remove('hidden');
        pokemonCard.classList.add('hidden');
        loadingSpinner.classList.add('hidden');
    }

    // Función para obtener el color según el tipo de Pokémon
    function getTypeColor(type) {
        const colors = {
            normal: '#A8A878',
            fire: '#F08030',
            water: '#6890F0',
            electric: '#F8D030',
            grass: '#78C850',
            ice: '#98D8D8',
            fighting: '#C03028',
            poison: '#A040A0',
            ground: '#E0C068',
            flying: '#A890F0',
            psychic: '#F85888',
            bug: '#A8B820',
            rock: '#B8A038',
            ghost: '#705898',
            dragon: '#7038F8',
            dark: '#705848',
            steel: '#B8B8D0',
            fairy: '#EE99AC'
        };
        return colors[type] || '#777777';
    }

    // Event listeners
    searchForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const searchTerm = searchInput.value.trim();
        if (searchTerm) {
            sortAndDisplayPokemon(searchTerm);
        }
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

    // Cargar la lista inicial de Pokémon
    loadInitialPokemon();
});