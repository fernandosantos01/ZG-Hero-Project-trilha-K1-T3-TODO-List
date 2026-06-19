const CHAVE_STORAGE = 'todolist_tarefas';

let listaTarefas = [];
let contadorId = 1;

const formularioTarefa    = document.getElementById('formulario-tarefa');
const inputId             = document.getElementById('id-tarefa');
const inputTitulo         = document.getElementById('titulo');
const inputDescricao      = document.getElementById('descricao');
const inputStatus         = document.getElementById('status');
const tituloFormulario    = document.getElementById('titulo-formulario');
const botaoCancelar       = document.getElementById('botao-cancelar');
const elementoListaTarefas = document.getElementById('lista-tarefas');
const selectFiltroStatus  = document.getElementById('filtro-status');
const controleSelecao     = document.getElementById('controle-selecao');
const selecionarTodas     = document.getElementById('selecionar-todas');
const barraSelecao        = document.getElementById('barra-selecao');
const contadorSelecionadas = document.getElementById('contador-selecionadas');
const statusMassa         = document.getElementById('status-massa');
const botaoAplicarMassa   = document.getElementById('botao-aplicar-massa');
const botaoLimparSelecao  = document.getElementById('botao-limpar-selecao');

// --- LocalStorage ---

function salvarNoStorage() {
    localStorage.setItem(CHAVE_STORAGE, JSON.stringify(listaTarefas));
}

function carregarDoStorage() {
    const dados = localStorage.getItem(CHAVE_STORAGE);
    if (dados) {
        listaTarefas = JSON.parse(dados);
        const maiorId = listaTarefas.reduce((max, t) => Math.max(max, t.id), 0);
        contadorId = maiorId + 1;
    }
}

// --- Formulário ---

formularioTarefa.addEventListener('submit', function (evento) {
    evento.preventDefault();

    const idAtual  = inputId.value;
    const novaTarefa = {
        titulo:    inputTitulo.value,
        descricao: inputDescricao.value,
        status:    inputStatus.value
    };

    if (idAtual) {
        const indice = listaTarefas.findIndex(t => t.id === parseInt(idAtual));
        if (indice !== -1) {
            listaTarefas[indice] = { ...novaTarefa, id: parseInt(idAtual) };
        }
    } else {
        novaTarefa.id = contadorId++;
        listaTarefas.push(novaTarefa);
    }

    salvarNoStorage();
    resetarFormulario();
    renderizarTarefas();
});

function prepararEdicao(id) {
    const tarefa = listaTarefas.find(t => t.id === id);
    if (!tarefa) return;

    inputId.value          = tarefa.id;
    inputTitulo.value      = tarefa.titulo;
    inputDescricao.value   = tarefa.descricao;
    inputStatus.value      = tarefa.status;
    tituloFormulario.textContent = 'Editar Tarefa';
    botaoCancelar.classList.remove('oculto');
}

function excluirTarefa(id) {
    if (!confirm('Tem certeza que deseja excluir esta tarefa?')) return;
    listaTarefas = listaTarefas.filter(t => t.id !== id);
    salvarNoStorage();
    renderizarTarefas();
}

function resetarFormulario() {
    formularioTarefa.reset();
    inputId.value = '';
    tituloFormulario.textContent = 'Nova Tarefa';
    botaoCancelar.classList.add('oculto');
}

// --- Seleção em massa ---

function getIdsSelecionados() {
    return [...document.querySelectorAll('.checkbox-tarefa:checked')]
        .map(cb => parseInt(cb.dataset.id));
}

function atualizarBarraSelecao() {
    const ids = getIdsSelecionados();
    const total = ids.length;

    if (total > 0) {
        barraSelecao.classList.remove('oculto');
        contadorSelecionadas.textContent =
            `${total} tarefa${total > 1 ? 's' : ''} selecionada${total > 1 ? 's' : ''}`;
    } else {
        barraSelecao.classList.add('oculto');
    }

    const checkboxes = document.querySelectorAll('.checkbox-tarefa');
    selecionarTodas.checked = checkboxes.length > 0 && total === checkboxes.length;
}

function alterarStatusEmMassa() {
    const ids = getIdsSelecionados();
    if (ids.length === 0) return;

    const novoStatus = statusMassa.value;
    listaTarefas = listaTarefas.map(t =>
        ids.includes(t.id) ? { ...t, status: novoStatus } : t
    );

    salvarNoStorage();
    renderizarTarefas();
}

selecionarTodas.addEventListener('change', function () {
    document.querySelectorAll('.checkbox-tarefa')
        .forEach(cb => cb.checked = this.checked);
    atualizarBarraSelecao();
});

botaoAplicarMassa.addEventListener('click', alterarStatusEmMassa);

botaoLimparSelecao.addEventListener('click', function () {
    document.querySelectorAll('.checkbox-tarefa').forEach(cb => cb.checked = false);
    selecionarTodas.checked = false;
    barraSelecao.classList.add('oculto');
});

// --- Renderização ---

function renderizarTarefas() {
    elementoListaTarefas.innerHTML = '';
    selecionarTodas.checked = false;
    barraSelecao.classList.add('oculto');

    const filtroAtual = selectFiltroStatus.value;
    const tarefasFiltradas = filtroAtual === 'TODOS'
        ? listaTarefas
        : listaTarefas.filter(t => t.status === filtroAtual);

    controleSelecao.classList.toggle('oculto', tarefasFiltradas.length === 0);

    if (tarefasFiltradas.length === 0) {
        const mensagem = document.createElement('li');
        mensagem.className = 'mensagem-vazia';
        mensagem.textContent = 'Nenhuma tarefa encontrada.';
        elementoListaTarefas.appendChild(mensagem);
        return;
    }

    tarefasFiltradas.forEach(tarefa => {
        const item = document.createElement('li');

        item.innerHTML = `
            <div class="selecao-tarefa">
                <input type="checkbox" class="checkbox-tarefa" data-id="${tarefa.id}">
            </div>
            <div class="detalhes-tarefa">
                <strong>${tarefa.titulo}</strong>
                <p>${tarefa.descricao}</p>
                <span class="status-badge status-${tarefa.status}">${tarefa.status}</span>
            </div>
            <div class="acoes-tarefa">
                <button class="botao-editar" onclick="prepararEdicao(${tarefa.id})">Editar</button>
                <button class="botao-excluir" onclick="excluirTarefa(${tarefa.id})">Excluir</button>
            </div>
        `;

        item.querySelector('.checkbox-tarefa').addEventListener('change', function () {
            item.classList.toggle('selecionada', this.checked);
            atualizarBarraSelecao();
        });

        elementoListaTarefas.appendChild(item);
    });
}

// --- Inicialização ---

botaoCancelar.addEventListener('click', resetarFormulario);
selectFiltroStatus.addEventListener('change', renderizarTarefas);

carregarDoStorage();
renderizarTarefas();
