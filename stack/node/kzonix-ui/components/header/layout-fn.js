import Header from './header'

const layoutStyle = {
  margin: 20,
  padding: 20,
  border: '1px solid #DDD'
}

const withHeader = Page => () => (
  <div style={layoutStyle}>
    <Header />
    <Page />
  </div>
)

export default withHeader
