const myMethods = {
    toast(msg,type){
        let strType = "success"
        switch(type){
            case 1:
                strType = "success"
                break
            case 2:
                strType = "error"
                break  
            default:
                strType = "success"    
        } 
        this.$message({
            message: msg,
            type: strType
        })
    },
    navigator(url){
        this.$router.push(url)
    },
    clone(obj) {
        return JSON.parse(JSON.stringify(obj))
    },
}

export default {
    data(){

        return {
           mix: 'mix',
         
        }
    },
    methods: {
        ...myMethods
    },
}