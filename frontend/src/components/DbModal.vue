<template>
    <el-dialog title="Edit" v-model="dialogFormVisible" :close-on-click-modal="false" :show-close="false">
        <el-form :model="form">
            <el-form-item label="item_id" :label-width="formLabelWidth">
                <el-input :disabled="true" v-model="form.id" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="username" :label-width="formLabelWidth">
                <el-input :disabled="false" v-model="form.username" auto-complete="off"></el-input>
            </el-form-item>

            <el-form-item label="email" :label-width="formLabelWidth">
                <el-input :disabled="false" v-model="form.email" auto-complete="off"></el-input>
            </el-form-item>

            <el-form-item label="phone" :label-width="formLabelWidth">
                <el-input v-model="form.phone" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="sex" :label-width="formLabelWidth">
                <el-input :disabled="false" v-model="form.sex" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="zone" :label-width="formLabelWidth">
                <el-input v-model="form.zone" auto-complete="off"></el-input>
            </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button :plain="true" type="danger" v-on:click="canclemodal">Cancel</el-button>
            <el-button :plain="true" @click="saveForm(form)">Save</el-button>
        </div>
    </el-dialog>
</template>


<script>
    export default {
        data(){
            return {
                formLabelWidth: '120px',
            }
        },
        props: ['dialogFormVisible', 'form'],

        methods: {
            saveForm: function (formName) {
                if (formName.id) {
                        this.updateForm(formName);
                    } else {
                        this.insertForm(formName);
                }
            },

            updateForm: function (formName) {
                let itemId = formName.id;
                let phone = formName.phone;
                let zone = formName.zone;
                this.$axios.put('http://127.0.0.1:8000/api/persons/detail/' + itemId, {
                    phone: phone,
                    zone: zone
                })
                    .then(function (response) {
                        console.log(response);
                        this.form = response.data;

                    })
                    .catch(function (error) {
                        console.log(error);
                    });
                location.reload();
            },

            insertForm: function (formName) {
                let username = formName.username;
                let email = formName.email;
                let phone = formName.phone;
                let sex = formName.sex;
                let zone = formName.zone;
                this.$axios.post('http://127.0.0.1:8000/api/persons/', {
                    username: username,
                    email: email,
                    phone: phone,
                    sex: sex,
                    zone: zone  
            })
            .then((response) => {
                console.log(response);
                this.form = response.data;
            })
            .catch((error) => {
                console.log(error);
            });
            location.reload();
            },

            canclemodal: function () {
                this.$emit('canclemodal');
            }
        }

    }

</script>