const myMethods = {
	toast(msg, type, position) {
		this.$refs.uToast.show({
			title: msg,
			type: type,
			position: position
		})
	},
	navigator(url) {
		uni.navigateTo({
			url: url,
			animationType: 'pop-in',
			animationDuration: 200
		});
	},

	
	
	
	
	clone(obj) {
	        return JSON.parse(JSON.stringify(obj))
	    },
	
	

	goBack() {
		uni.navigateBack({
			delta: 1
		});
	}

}

export default {
	data() {

		return {
			mix: 'mix',

		}
	},
	methods: {
		...myMethods
	},
}
