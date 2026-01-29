import { Component, OnInit, ViewChild, inject } from "@angular/core";
import { TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { Rol } from "../../modules/rol.model";
import { UtilService } from "../../service/utilService";
import { UsuarioService } from "../../service/usuarioService";
import { UsuarioDTO } from "../../modules/DTO/UsuarioDTO";
import { DialogModule } from "primeng/dialog";
import { MessageService } from "primeng/api";
import { FloatLabelModule } from "primeng/floatlabel";
import { ToastModule } from "primeng/toast";
import { MessageModule } from "primeng/message";
import { ToggleSwitch } from "primeng/toggleswitch";
import { finalize } from "rxjs/operators";
import { TipoDocumento } from "../../modules/tipoDocumento.model";
import { CheckboxModule } from 'primeng/checkbox';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { RolConAsignacion } from "../../modules/RolConAsignacion.modul";
import { ConfirmationService } from "primeng/api";
import { ConfirmDialog } from "primeng/confirmdialog";


@Component({
    selector: 'app-gestion-usuario',
    templateUrl: './gestion-usuario.component.html',
    styleUrls: ['./gestion-usuario.component.css'],
    standalone: true,
    imports: [TableModule, ButtonModule, CheckboxModule, ProgressSpinnerModule, ToggleSwitch, MessageModule, InputTextModule, SelectModule, FloatLabelModule, CommonModule, FormsModule, DialogModule, ToastModule, ConfirmDialog],
    providers: [MessageService, ConfirmationService]
})

export class GestionUsuarioComponent implements OnInit {
    messageService = inject(MessageService);
    confirmationService = inject(ConfirmationService);
    @ViewChild('usuarioFormRef') usuarioFormRef!: NgForm;


    eliminando: boolean = false;

    // Estados de la UI
    cargando: boolean = false;
    guardando: boolean = false;

    rolesUsuarioIds: number[] = [];
    rolesConAsignacion: RolConAsignacion[] = [];
    guardandoRoles: boolean = false;

    // Datos
    usuarios: UsuarioDTO[] = [];
    roles: Rol[] = [];
    selectedRol: Rol | null = null;
    estado = [
        { label: 'Todos los estados', value: -1 },
        { label: 'Activo', value: 1 },
        { label: 'Inactivo', value: 0 }
    ];
    selectedEstado: any = null;
    value: string = '';
    //tipos de documentos
    tiposDocumento: TipoDocumento[] = [];
    selectedTipoDocumento: TipoDocumento | null = null;

     displayModal: boolean = false;
    rolesModal: boolean = false;
    modalTitle: string = '';
    modoEdicion: boolean = false;
    usuarioIdEditando: number | null = null;
    usuarioSeleccionado: UsuarioDTO | null = null;
    // Estados para el formulario
    estadosFormulario = [
        { label: 'Activo', value: 1 },
        { label: 'Inactivo', value: 0 }
    ];

    // Objeto para el formulario del usuario
    usuarioForm = this.inicializarFormulario();

    cargandoRoles: boolean = false;

    // Nueva propiedad para el término de búsqueda
    rolSearchTerm: string = '';
    // Propiedad para los roles filtrados
    rolesConAsignacionFiltrados: RolConAsignacion[] = [];

    constructor(private utilService: UtilService,
        private usuarioService: UsuarioService
    ) { }

    ngOnInit(): void {
        this.selectedEstado = this.estado[0];
        this.cargarRoles();
        this.cargarTiposDocumento();
    }

    inicializarFormulario() {
        return {
            idUsuario: null as number | null,
            nombres: '',
            apellidos: '',
            login: '',
            correo: '',
            idTipoDoc: null as number | null,
            numDoc: '',
            celular: '',
            password: '',
            estado: true,
            tipoDocumento: '',
            rolesString: ''
        };
    }

    abrirModal(title: string, usuario?: UsuarioDTO): void {
        this.modalTitle = title;

        // Verificar si se trata del modal de roles o del modal de usuario
        if (title === 'Asignar Roles') {
            this.abrirModalRoles(usuario);
        } else {
            this.abrirModalUsuario(title, usuario);
        }
    }

    // Método para abrir el modal de usuario (crear/editar)
    abrirModalUsuario(title: string, usuario?: UsuarioDTO): void {
        this.displayModal = true;
        this.usuarioForm = this.inicializarFormulario();
        this.selectedTipoDocumento = null; // Resetear el tipo de documento seleccionado

        // Si hay un usuario, estamos en modo edición
        if (usuario && usuario.idUsuario) {
            console.log('🔄 Editando usuario con ID:', usuario.idUsuario);
            this.modoEdicion = true;
            this.usuarioIdEditando = usuario.idUsuario;
            this.cargarDatosUsuario(usuario.idUsuario);
        } else {
            this.modoEdicion = false;
            this.usuarioIdEditando = null;

            // Reset del formulario para nueva entrada
            setTimeout(() => {
                if (this.usuarioFormRef) {
                    this.usuarioFormRef.resetForm(this.usuarioForm);
                }
            });
        }
    }

    // Nuevo método para abrir el modal de roles
    abrirModalRoles(usuario?: UsuarioDTO): void {
        if (!usuario || !usuario.idUsuario) {
            this.mostrarMensaje('error', 'Error', 'No se pudo identificar el usuario');
            return;
        }

        this.rolesModal = true;
        this.usuarioIdEditando = usuario.idUsuario;
        this.usuarioSeleccionado = usuario;
        this.cargarRolesYAsignaciones(usuario.idUsuario);

    }


    cargarDatosUsuario(id: number): void {
        this.cargando = true;
        this.usuarioService.obtenerUsuarioPorId(id)
            .pipe(finalize(() => this.cargando = false))
            .subscribe({
                next: (usuario) => {
                    console.log('📋 Datos de usuario cargados:', usuario);
                    if (usuario) {
                        // Obtener el ID del tipo de documento
                        const idTipoDoc = this.obtenerIdTipoDocumento(usuario.tipoDocumento);
                        console.log('🆔 Tipo Documento identificado:', idTipoDoc);

                        // Actualizar el formulario con todos los datos
                        this.usuarioForm = {
                            idUsuario: usuario.idUsuario,
                            nombres: usuario.nombres || '',
                            apellidos: usuario.apellidos || '',
                            login: usuario.login || '',
                            correo: usuario.correo || '',
                            idTipoDoc: idTipoDoc,
                            numDoc: usuario.numDoc || '',
                            celular: usuario.celular || '',
                            password: '',  // No cargar contraseña por seguridad
                            estado: usuario.estado === 1,
                            tipoDocumento: usuario.tipoDocumento || '',
                            rolesString: usuario.rolesString || ''
                        };

                        // Actualizar el tipo de documento seleccionado
                        if (idTipoDoc) {
                            this.selectedTipoDocumento = this.tiposDocumento.find(td => td.idTipoDoc === idTipoDoc) || null;
                            console.log('🔍 Tipo de documento seleccionado:', this.selectedTipoDocumento);
                        }

                        // Reset del formulario con los datos cargados
                        setTimeout(() => {
                            if (this.usuarioFormRef) {
                                this.usuarioFormRef.form.markAsPristine();
                            }
                        });
                    }
                },
                error: (err) => {
                    console.error('Error al cargar usuario:', err);
                    this.mostrarMensaje('error', 'Error', 'No se pudo cargar la información del usuario');
                }
            });
    }
    // Método para cargar los roles para el modal
    cargarRolesYAsignaciones(idUsuario: number): void {
        this.cargandoRoles = true;
        this.rolSearchTerm = ''; // Reiniciar búsqueda

        // Primero cargamos todos los roles disponibles
        this.utilService.listarRoles()
            .subscribe({
                next: (roles: Rol[]) => {
                    // Convertimos los roles normales a roles con asignación
                    this.rolesConAsignacion = roles
                        .filter(rol => rol.idRol !== -1) // Filtrar el rol "Todos" si existe
                        .map(rol => ({
                            ...rol,
                            asignado: false // Por defecto ninguno está asignado
                        }));

                    // Inicializar los roles filtrados
                    this.rolesConAsignacionFiltrados = [...this.rolesConAsignacion];

                    // Ahora cargamos los roles asignados al usuario
                    this.cargarRolesAsignadosAlUsuario(idUsuario);
                },
                error: (err) => {
                    console.error('Error al cargar roles:', err);
                    this.mostrarMensaje('error', 'Error', 'No se pudieron cargar los roles');
                    this.cargandoRoles = false;
                }
            });
    }
    // Método para filtrar los roles según el término de búsqueda
    filtrarRoles(): void {
        if (!this.rolSearchTerm.trim()) {
            this.rolesConAsignacionFiltrados = [...this.rolesConAsignacion];
            return;
        }

        const searchTerm = this.rolSearchTerm.toLowerCase();
        this.rolesConAsignacionFiltrados = this.rolesConAsignacion.filter(rol => 
            rol.nombre.toLowerCase().includes(searchTerm)
        );
    }

    // Método para obtener un ícono según el nombre del rol
    getRoleIcon(roleName: string): string {
        const roleIconMap: {[key: string]: string} = {
            'ADMIN': 'fa-user-cog',
            'ADMINISTRADOR': 'fa-user-cog',
            'USER': 'fa-user',
            'USUARIO': 'fa-user',
            'MODERATOR': 'fa-user-shield',
            'MODERADOR': 'fa-user-shield',
            'EDITOR': 'fa-edit',
            'VIEWER': 'fa-eye',
            'SUPERVISOR': 'fa-user-tie'
        };

        // Buscar coincidencia parcial
        for (const [key, icon] of Object.entries(roleIconMap)) {
            if (roleName.toUpperCase().includes(key)) {
                return icon;
            }
        }
        
        return 'fa-user-tag'; // Icono por defecto
    }

    // Método para reaccionar al cambio de selección de roles
    onRoleSelectionChange(rol: RolConAsignacion): void {
        console.log(`Rol ${rol.nombre} ${rol.asignado ? 'seleccionado' : 'deseleccionado'}`);
        // Aquí podrías implementar lógica adicional si es necesario
    }

    // Método para deseleccionar un rol desde la etiqueta
    deselectRole(rol: RolConAsignacion): void {
        rol.asignado = false;
    }

    // Método para obtener la cantidad de roles seleccionados
    getSelectedRolesCount(): number {
        return this.rolesConAsignacion.filter(rol => rol.asignado).length;
    }

    // Método para obtener los roles seleccionados
    getSelectedRoles(): RolConAsignacion[] {
        return this.rolesConAsignacion.filter(rol => rol.asignado);
    }

    guardarRolesAsignados(): void {
        if (!this.usuarioIdEditando) {
            this.mostrarMensaje('error', 'Error', 'No se pudo identificar el usuario');
            return;
        }

        //vamos a obtener los roles seleccionados
        const rolesSeleccionados = this.rolesConAsignacion
            .filter(rol => rol.asignado)
            .map(rol => rol.idRol);
        console.log('Roles seleccionados para guardar:', rolesSeleccionados);
        this.guardandoRoles = true;


        // Llamar al servicio para actualizar los roles del usuario
        this.usuarioService.actualizarRolesUsuario(this.usuarioIdEditando, rolesSeleccionados)
            .pipe(finalize(() => this.guardandoRoles = false))
            .subscribe({
                next: () => {
                    this.mostrarMensaje('success', 'Éxito', 'Roles actualizados correctamente');
                    this.cerrarModal();
                },
                error: (err) => {
                    console.error('Error al actualizar roles:', err);
                    this.mostrarMensaje('error', 'Error', 'No se pudieron actualizar los roles');
                }
            });
    }

    obtenerIdTipoDocumento(tipoDoc: string): number | null {
        // Mapea el nombre del documento a su ID
        const mapTipoDoc: { [key: string]: number } = {
            'DNI': 1,
            'Pasaporte': 2,
            'Cedula': 3
        };

        return mapTipoDoc[tipoDoc] || null;
    }

    cerrarModal(): void {
        this.displayModal = false;
        this.rolesModal = false;
        this.cargarUsuarios();
    }

    cargarRoles(): void {
        this.cargando = true;
        this.utilService.listarRoles()
            .pipe(finalize(() => this.cargando = false))
            .subscribe({
                next: (data: Rol[]) => {
                    //todos los roles
                    this.roles = [{ idRol: -1, nombre: 'Todos los roles', estado: 1 }, ...data];
                    this.selectedRol = this.roles[0];
                    this.cargarUsuarios();
                },
                error: (err) => {
                    console.error('Error al cargar roles:', err);
                    this.mostrarMensaje('error', 'Error', 'No se pudieron cargar los roles');
                }
            });
    }

    cargarTiposDocumento(): void {
        this.cargando = true;
        this.utilService.listarTiposDocumento()
            .pipe(finalize(() => this.cargando = false))
            .subscribe({
                next: (data: TipoDocumento[]) => {
                    this.tiposDocumento = data || [];
                    this.selectedTipoDocumento = this.tiposDocumento[0] || null;
                },
                error: (err) => {
                    console.error('Error al cargar tipos de documento:', err);
                    this.mostrarMensaje('error', 'Error', 'No se pudieron cargar los tipos de documento');
                }
            });
    }


    cargarUsuarios(): void {
        // Lógica para cargar usuarios con los filtros seleccionados
        const idRol = this.selectedRol ? this.selectedRol.idRol : -1;
        const estado = this.selectedEstado ? this.selectedEstado.value : -1;
        const filtro = this.value || '';

        this.cargando = true;
        this.usuarioService.listarUsuariosConFiltros(idRol, estado, filtro)
            .pipe(finalize(() => this.cargando = false))
            .subscribe({
                next: (data: UsuarioDTO[]) => {
                    this.usuarios = data || [];
                },
                error: (err) => {
                    console.error('Error al cargar usuarios:', err);
                    this.mostrarMensaje('error', 'Error', 'No se pudieron cargar los usuarios');
                }
            });
    }

    // Métodos para manejar los filtros
    onRoleChange(event: any): void {
        this.selectedRol = event.value;
        this.cargarUsuarios();
    }

    onEstadoChange(event: any): void {
        this.selectedEstado = event.value;
        this.cargarUsuarios();
    }


    onFiltroChange(): void {
        this.cargarUsuarios();
    }

    // Sincronizar el tipo de documento seleccionado con el formulario
    onTipoDocumentoChange(event: any): void {
        if (event && event.value) {
            this.usuarioForm.idTipoDoc = event.value.idTipoDoc;
            console.log('📝 Tipo de documento actualizado:', this.usuarioForm.idTipoDoc);
        }
    }

    onSubmit(): void {
        // Si el formulario no está disponible aún, salimos
        if (!this.usuarioFormRef) {
            return;
        }

        // Marcar campos como tocados para mostrar validaciones
        Object.keys(this.usuarioFormRef.controls).forEach(key => {
            const control = this.usuarioFormRef.controls[key];
            control.markAsTouched();
        });

        if (this.usuarioFormRef.valid) {
            // Asegurarnos de tener el idTipoDoc actualizado desde el selector
            if (this.selectedTipoDocumento) {
                this.usuarioForm.idTipoDoc = this.selectedTipoDocumento.idTipoDoc;
            }

            // Asegurarnos de tener el idUsuario correcto en modo edición
            if (this.modoEdicion && this.usuarioIdEditando) {
                this.usuarioForm.idUsuario = this.usuarioIdEditando;
            }

            // Preparar objeto para enviar
            const usuarioData: any = {
                idUsuario: this.usuarioForm.idUsuario,
                nombres: this.usuarioForm.nombres,
                apellidos: this.usuarioForm.apellidos,
                login: this.usuarioForm.login,
                correo: this.usuarioForm.correo,
                idTipoDoc: this.usuarioForm.idTipoDoc,
                numDoc: this.usuarioForm.numDoc,
                celular: this.usuarioForm.celular,
                estado: this.usuarioForm.estado ? 1 : 0
            };

            // Agregar contraseña solo si está presente
            if (this.usuarioForm.password) {
                usuarioData.password = this.usuarioForm.password;
            }

            // Log detallado de los datos que se van a enviar
            console.log('📤 ENVIANDO DATOS:', usuarioData);
            console.log(`Operación: ${this.modoEdicion ? 'ACTUALIZAR' : 'CREAR'} usuario`);

            // Validar que tenemos los campos críticos antes de enviar
            if (!usuarioData.idTipoDoc) {
                this.mostrarMensaje('error', 'Error', 'Falta seleccionar el tipo de documento');
                console.error('❌ ERROR: Falta idTipoDoc');
                return;
            }

            if (this.modoEdicion && !usuarioData.idUsuario) {
                this.mostrarMensaje('error', 'Error', 'No se pudo determinar el ID del usuario a actualizar');
                console.error('❌ ERROR: Falta idUsuario en modo edición');
                return;
            }

            // Decidir si crear o actualizar
            this.guardando = true;
            const operacion = this.modoEdicion ?
                this.usuarioService.actualizarUsuario(usuarioData) :
                this.usuarioService.guardarUsuario(usuarioData);

            operacion
                .pipe(finalize(() => this.guardando = false))
                .subscribe({
                    next: (response) => {
                        console.log('✅ RESPUESTA DEL SERVIDOR:', response);
                        console.log(`Usuario ${this.modoEdicion ? 'actualizado' : 'guardado'} correctamente`);

                        this.mostrarMensaje(
                            'success',
                            'Éxito',
                            `Usuario ${this.modoEdicion ? 'actualizado' : 'guardado'} correctamente`
                        );
                        this.cargarUsuarios();
                        this.cerrarModal();
                    },
                    error: (err) => {
                        console.error('❌ ERROR DEL SERVIDOR:', err);
                        console.error(`Error al ${this.modoEdicion ? 'actualizar' : 'guardar'} usuario:`, err);

                        this.mostrarMensaje(
                            'error',
                            'Error',
                            `No se pudo ${this.modoEdicion ? 'actualizar' : 'guardar'} el usuario: ${err.message || err.statusText || 'Error desconocido'}`
                        );
                    }
                });
        } else {
            console.warn('❗ FORMULARIO INVÁLIDO - Campos con errores:',
                Object.keys(this.usuarioFormRef.controls)
                    .filter(key => this.usuarioFormRef.controls[key].invalid)
                    .map(key => `${key}: ${JSON.stringify(this.usuarioFormRef.controls[key].errors)}`)
            );

            this.mostrarMensaje(
                'error',
                'Error',
                'Por favor complete todos los campos requeridos correctamente'
            );
        }
    }

    // Método de utilidad para mostrar mensajes
    mostrarMensaje(severity: string, summary: string, detail: string): void {
        this.messageService.add({
            severity,
            summary,
            detail,
            life: 2000
        });
    }

    confirmarEliminarUsuario(usuario: UsuarioDTO): void {
        if (!usuario || !usuario.idUsuario) {
            this.mostrarMensaje('error', 'Error', 'No se pudo identificar el usuario a eliminar');
            return;
        }

        this.confirmationService.confirm({
            header: 'Eliminar usuario',
            message: `¿Estás seguro que deseas eliminar al usuario ${usuario.nombres} ${usuario.apellidos}?`,
            acceptLabel: 'Sí, eliminar',
            rejectLabel: 'No, cancelar',
            acceptButtonStyleClass: 'p-button-danger',
            rejectButtonStyleClass: 'p-button-text',
            accept: () => {
                this.eliminarUsuario(usuario.idUsuario!);
            },
            reject: () => {
                this.messageService.add({
                    severity: 'info',
                    summary: 'Cancelado',
                    detail: 'Eliminación de usuario cancelada',
                    life: 2000
                });
            }
        });
    }

    eliminarUsuario(idUsuario: number): void {
        this.eliminando = true;

        this.usuarioService.eliminarUsuario(idUsuario)
            .pipe(finalize(() => this.eliminando = false))
            .subscribe({
                next: (response) => {
                    console.log('✅ Usuario eliminado correctamente:', response);
                    this.mostrarMensaje('success', 'Éxito', 'Usuario eliminado correctamente');
                    this.cargarUsuarios(); // Recargar la lista de usuarios
                },
                error: (err) => {
                    console.error('❌ Error al eliminar usuario:', err);

                    // Verificar si es un error 401 y si se trata de eliminar el mismo usuario
                    if (err.status === 401) {
                        // Suponemos que un 401 al intentar eliminar es porque se intenta eliminar el propio usuario
                        this.mostrarMensaje(
                            'error',
                            'Acción no permitida',
                            'No puedes eliminar tu propio usuario mientras estás conectado'
                        );
                    } else {
                        // Mensaje genérico para otros tipos de errores
                        let mensajeError = 'No se pudo eliminar el usuario';

                        if (err.error && typeof err.error === 'string') {
                            mensajeError += ': ' + err.error;
                        } else if (err.message) {
                            mensajeError += ': ' + err.message;
                        } else if (err.statusText) {
                            mensajeError += ': ' + err.statusText;
                        }

                        this.mostrarMensaje('error', 'Error', mensajeError);
                    }
                }
            });
    }

    // Método para cargar los IDs de roles asignados al usuario
    cargarRolesAsignadosAlUsuario(idUsuario: number): void {
        this.usuarioService.obtenerRolesUsuarioIds(idUsuario)
            .pipe(finalize(() => this.cargandoRoles = false))
            .subscribe({
                next: (idsRoles: number[]) => {
                    console.log('IDs de roles asignados:', idsRoles);
                    this.rolesUsuarioIds = idsRoles;

                    // Marcar como asignados los roles que el usuario ya tiene
                    this.rolesConAsignacion.forEach(rol => {
                        rol.asignado = this.rolesUsuarioIds.includes(rol.idRol);
                    });
                    
                    // Actualizar también los roles filtrados para mantener la coherencia
                    this.rolesConAsignacionFiltrados = [...this.rolesConAsignacion];
                },
                error: (err) => {
                    console.error('Error al cargar roles asignados:', err);
                    this.mostrarMensaje('error', 'Error', 'No se pudieron cargar los roles asignados');
                }
            });
    }

}