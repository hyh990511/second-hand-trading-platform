<template>
    
</template>

<script>
    export default {
        name: "refresh",
        data() {
            return{
                Param: null,
                Destination: null,
            }
        },
        created() {
            this.Destination = this.$route.query.destination;
            this.Param = this.$route.query.param;
            this.$router.push({
                path: this.Destination,
                query:{
                    param: this.Param,
                }
            })
        }
    }
</script>

<style scoped>

</style>