<template>
    <el-form :inline="true" class="form-inline-container" :model="formInline">
        <el-form-item label="Sex">
            <el-select v-model="formInline.sex" clearable placeholder="select sex"
                       v-on:visible-change="selectDemo">
                <el-option
                        v-for="(item, index) in type_options"
                        :key = "index"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>

    <el-form-item label="Description">
        <el-input v-model="formInline.email" :disabled="!formInline.sex" placeholder="Please input suffix of email"></el-input>
    </el-form-item>
      <el-button fixed="right" class="button-container" type="primary" @click="addData()">Add Data</el-button>
    </el-form>
</template>

<script>
    import lodash from 'lodash'
    import Bus from '../eventBus'

    export default {
        name: 'db-filterinput',
        data() {
            return {
                type_options: [],
                formInline: {
                    sex: '',
                    email: ''
                },
                formLabelWidth: '120px'
            }
        },

        watch: {
            'formInline.sex': 'filterResultData',
            'formInline.email': 'filterResultData'
        },

        methods: {
            selectDemo: function (params) {
                if (params) {
                    this.$axios.get("http://127.0.0.1:8000/api/persons/sex")
                        .then((response) => {
                            this.type_options = response.data;
                            console.log(response.data);
                        }).catch(function (response) {
                        console.log(response)
                    });
                }

            },
            filterResultData: _.debounce(
                function () {
                    this.$axios.get("http://127.0.0.1:8000/api/persons", {
                        params: {
                            sex: this.formInline.sex,
                            email: this.formInline.email,
                        }
                    }).then((response) => {
                        response.data['sex'] = this.formInline.sex;
                        response.data['email'] = this.formInline.email;
                        Bus.$emit('filterResultData', response.data);
                        console.log(response.data);
                    }).catch(function (response) {
                        console.log(response)
                    });

                },
                500
            ),

            addData: function(){                                      
                Bus.$emit('callOtherMethod');
            }
        }
    }


</script>

<style scoped>

.button-container {
  margin-left: auto; /* This pushes the button to the right */
}
.form-inline-container {
  display: flex;
  flex-wrap: wrap;
  width: 100%; /* Make the form occupy 100% width */
  justify-content: space-between;
  align-items: center;
}
</style>