import { withHeader } from '../components/header/layout'

class About extends React.Component {
  pageContent () {
    return (
      <p>
        About us !
      </p>
    )
  }

  render () {
    return withHeader(this.pageContent)
  }
}
export default About
